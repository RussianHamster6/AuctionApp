package Handlers;

import Models.Auction;
import Models.Bid;
import Repository.AuctionRepository;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionHandler implements Runnable{

    private Socket socket;
    public ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<AuctionHandler> clients;
    private Auction auction;
    public Boolean terminateFlag = true;
    private AuctionRepository auctionRepository;

    public AuctionHandler(Socket client, ArrayList<AuctionHandler> clients, Auction auction, AuctionRepository repository) throws IOException {
        this.socket = client;
        this.clients = clients;
        this.auction = auction;
        this.auctionRepository = repository;
        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();
        out = new ObjectOutputStream(outputStream);
        in = new ObjectInputStream(inputStream);
    }

    @Override
    public void run() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, auction.auctionEndDateTime);

        service.schedule(new AuctionTerminationHandler(this), duration.getSeconds(), TimeUnit.SECONDS);

        try{
            while(terminateFlag){
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
        finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
