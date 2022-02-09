package Main;
import Handlers.AuctionHandler;
import Models.Auction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static ArrayList<AuctionHandler> auctionHandlers = new ArrayList<AuctionHandler>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    private static final int port = 9090;
    private static Auction tempAuc = new Auction("Temp", 1, 10,1);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);

        //TEMP


        while(true){
            System.out.println("Waiting for client connection...");

            //accepts new client connection
            Socket client = listener.accept();
            System.out.println("Connected to client!");

            //Get Auction They want to Connect to

            //adds new auctionThread to auctionHandlers list
            AuctionHandler auctionThread = new AuctionHandler(client, auctionHandlers, tempAuc);
            auctionHandlers.add(auctionThread);

            pool.execute(auctionThread);
        }

    }
}
