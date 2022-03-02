package Main;
import Handlers.AuctionHandler;
import Models.Auction;
import Repository.AuctionRepository;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static ArrayList<AuctionHandler> auctionHandlers = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(50);
    private static final int port = 9090;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);

        //Mock auctionRepository
        AuctionRepository auctionRepository = Mockito.mock(AuctionRepository.class);
        //AuctionList config
        ArrayList<Auction> aucList = new ArrayList<>();

        configureMocks(aucList, auctionRepository);

        while(true){
            System.out.println("Waiting for client connection...");

            //accepts new client connection
            Socket client = listener.accept();
            System.out.println("Connected to client!");

            //adds new auctionThread to auctionHandlers list
            AuctionHandler auctionThread = new AuctionHandler(client, auctionHandlers, auctionRepository);

            auctionHandlers.add(auctionThread);

            pool.execute(auctionThread);
        }

    }

    public static void configureMocks(ArrayList<Auction> aucList, AuctionRepository auctionRepository){
        //Add mock auctions to aucList
        aucList.add(new Auction("Temp", 1, 10,1));
        aucList.add(new Auction("Auction for Stuff",10,100,5));
        aucList.add(new Auction("Ended Auction", 1, 0, -1));
        //GetAuctionByName mocks
        Mockito.when(auctionRepository.getAuctionByName("Temp")).thenReturn(aucList.get(0));
        Mockito.when(auctionRepository.getAuctionByName(("Auction for Stuff"))).thenReturn(aucList.get(1));
        Mockito.when(auctionRepository.getAuctionByName(("Ended Auction"))).thenReturn(aucList.get(2));
        //GetAllAuctionMock
        Mockito.when(auctionRepository.getAllAuctions()).thenReturn(aucList);
    }
}
