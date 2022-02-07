package Handlers;

import Models.Auction;
import Models.Bid;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class AuctionHandler implements Runnable{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<AuctionHandler> clients;
    private Auction auction;

    public AuctionHandler(Socket client, ArrayList<AuctionHandler> clients, Auction auction) throws IOException {
        this.socket = client;
        this.clients = clients;
        this.auction = auction;
        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();
        out = new ObjectOutputStream(outputStream);
        in = new ObjectInputStream(inputStream);
    }

    @Override
    public void run() {
        try{
            while(true){
                Bid input = (Bid) in.readObject();

                if(input.amount > this.auction.currentHighBid.amount){
                    updateAuction(new Bid(input.bidder, input.amount));
                }
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void updateAuction(Bid newBid) throws IOException {
        for (AuctionHandler aHandler : clients ){
            aHandler.auction.registerBid(newBid);
            aHandler.out.writeObject(aHandler.auction);
        }
    }
}
