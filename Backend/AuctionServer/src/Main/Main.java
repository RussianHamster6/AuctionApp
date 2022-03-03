package Main;
import Handlers.AuctionHandler;
import Models.Auction;
import Models.Login;
import Repository.AuctionRepository;
import Repository.UserRepository;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.anyString;

public class Main {

    private static ArrayList<AuctionHandler> auctionHandlers = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(50);
    private static final int port = 9090;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);

        //Mock auctionRepository
        AuctionRepository auctionRepository = Mockito.mock(AuctionRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        //AuctionList config
        ArrayList<Auction> aucList = new ArrayList<>();
        ArrayList<Login> loginList = new ArrayList<>();

        configureMocks(aucList, auctionRepository, loginList, userRepository);

        while(true){
            System.out.println("Waiting for client connection...");

            //accepts new client connection
            Socket client = listener.accept();
            System.out.println("Connected to client!");

            //adds new auctionThread to auctionHandlers list
            AuctionHandler auctionThread = new AuctionHandler(client, auctionHandlers, auctionRepository, userRepository);

            auctionHandlers.add(auctionThread);

            pool.execute(auctionThread);
        }

    }

    public static void removeClient(AuctionHandler client){
        auctionHandlers.remove(client);
    }

    public static void configureMocks(ArrayList<Auction> aucList, AuctionRepository auctionRepository, ArrayList<Login>loginList, UserRepository userRepository){
        //Add mock auctions to aucList
        aucList.add(new Auction("Temp", 1, 10,1));
        aucList.add(new Auction("Auction for Stuff",10,100,5));
        aucList.add(new Auction("Ended Auction", 1, 0, -1));
        //GetAuctionByName mocks
        for(Auction auc: aucList){
            Mockito.when(auctionRepository.getAuctionByName(auc.itemName)).thenReturn(auc);
        }
        //GetAllAuctionMock
        Mockito.when(auctionRepository.getAllAuctions()).thenReturn(aucList);
        //Mock Users
        loginList.add(new Login("user", "pass"));
        loginList.add(new Login("secondUser", "pass2"));
        //GetUserByName mocks
        Mockito.when(userRepository.getUserByUsername(anyString())).thenReturn(null);
        for(Login login: loginList){
            Mockito.when(userRepository.getUserByUsername(login.userName)).thenReturn(login);
        }
    }
}
