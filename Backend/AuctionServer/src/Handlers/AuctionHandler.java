package Handlers;

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
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;

    public AuctionHandler(Socket client, ArrayList<AuctionHandler> clients, AuctionRepository repository, UserRepository userRepository) throws IOException {
        this.socket = client;
        this.clients = clients;
        this.auctionRepository = repository;
        this.userRepository = userRepository;
        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();
        out = new ObjectOutputStream(outputStream);
        in = new ObjectInputStream(inputStream);
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
                    if(aucString.equals("GOINGBACK")){
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
                    else if(aucString.equals("notificationGoingBack")){
                        //Need to do this on the client
                        terminateFlag = false;
                        clients.remove(this);
                        Main.Main.removeClient(this);
                    }
                }
                else if(x.isInstance(new ArrayList<AuctionTableRow>())){
                    //if arrayList of auctions

                    ArrayList<AuctionTableRow> tableRows = (ArrayList<AuctionTableRow>) input;
                    ArrayList<Auction> auctionsToSend = new ArrayList<Auction>();

                    for(AuctionTableRow ATR: tableRows){
                        auctionsToSend.add(auctionRepository.getAuctionByName(ATR.getItemName()));
                    }

                    out.writeObject(auctionsToSend);
                }
                else if(x.isInstance(new Login("User","pass"))){
                    Login loginAttempt = (Login) input;
                    clients.remove(this);
                    Main.Main.removeClient(this);
                    //get login
                    Login fromRepo = this.userRepository.getUserByUsername(loginAttempt.userName);
                    //if get login is not null then send true
                    if(fromRepo != null && fromRepo.password.equals(loginAttempt.password)){
                        out.writeObject(true);
                    }
                    //else send false
                    else{
                        out.writeObject(false);
                    }
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
