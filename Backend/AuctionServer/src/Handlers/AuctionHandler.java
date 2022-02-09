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
                Object input = in.readObject();
                var x = input.getClass();
                if(x.isInstance("string")){
                    out.writeObject(auction);
                }
                else{
                    Bid bid = (Bid) input;
                    System.out.println(bid.amount);
                    if (bid.amount > this.auction.currentHighBid.amount) {
                        updateAuction(bid);
                    }
                }
            }
        }
        catch (IOException | ClassNotFoundException | ClassCastException e){
            e.printStackTrace();
        }
    }

    private void updateAuction(Bid newBid) throws IOException {
        this.auction.registerBid(newBid);

        for (AuctionHandler aHandler : clients ){
            aHandler.out.reset();
            aHandler.auction = this.auction;
            aHandler.out.writeObject(this.auction);
        }
        //Write to mocking framework
    }
}
