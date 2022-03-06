package Handlers;

import Helpers.AuctionHandlerHelpers;
import Models.*;
import Repository.AuctionRepository;
import Repository.UserRepository;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionHandler implements Runnable{

    private Socket socket;
    public ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<AuctionHandler> clients;
    //private ArrayList<>
    private Auction auction;
    public Boolean terminateFlag = true;
    public AuctionRepository auctionRepository;
    public UserRepository userRepository;

    public AuctionHandler(Socket client, ArrayList<AuctionHandler> clients, AuctionRepository repository, UserRepository userRepository) throws IOException {
        this.socket = client;
        this.clients = clients;
        this.auctionRepository = repository;
        this.userRepository = userRepository;
        if(client.isConnected()){
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();
            out = new ObjectOutputStream(outputStream);
            in = new ObjectInputStream(inputStream);
        }
    }

    @Override
    public void run() {

        try{
            while(terminateFlag){
                Object input = in.readObject();
                var x = input.getClass();
                if(x.isInstance("string")){
                    String aucString = input.toString();
                    //Get the Auction they want to connect to
                    this.auction = this.auctionRepository.getAuctionByName(aucString);
                    out.writeObject(auction);
                    ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

                    LocalDateTime now = LocalDateTime.now();
                    Duration duration = Duration.between(now, auction.auctionEndDateTime);

                    service.schedule(new AuctionTerminationHandler(this), duration.getSeconds(), TimeUnit.SECONDS);
                }
                else if (x.isInstance(new AuctionConnectionDetails("foo"))) {
                    AuctionConnectionDetails ACD = (AuctionConnectionDetails) input;
                    String aucString = ACD.action;
                    if(aucString.equals("GOINGBACK") || aucString.equals("notificationGoingBack")){
                        clients.remove(this);
                        Main.Main.removeClient(this);
                        terminateFlag = false;
                    }
                    else if(aucString.equals("GETALLAUC")){
                        //gets all auctions and writes that out
                        ArrayList<Auction> allAuctions = this.auctionRepository.getAllAuctions();
                        out.writeObject(allAuctions);
                        clients.remove(this);
                        //sets terminate flag to false so the thread closes.
                        terminateFlag = false;
                    }
                }
                else if(x.isInstance(new ArrayList<AuctionTableRow>())){
                    //if arrayList of auctions

                    ArrayList<AuctionTableRow> tableRows = (ArrayList<AuctionTableRow>) input;

                    out.writeObject(AuctionHandlerHelpers.GetAuctionsFromTableRows(tableRows, auctionRepository));
                }
                else if(x.isInstance(new Login("User","pass"))){
                    Login loginAttempt = (Login) input;
                    out.writeObject(AuctionHandlerHelpers.ValidLogin(loginAttempt, clients, this));
                }
                else{
                    Bid bid = (Bid) input;
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

        for (AuctionHandler aHandler : clients){
            aHandler.out.reset();
            aHandler.auction = this.auction;
            aHandler.out.writeObject(this.auction);
        }
    }

}
