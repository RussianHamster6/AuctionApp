package Handlers;

import Models.Auction;

import java.io.IOException;

public class AuctionTerminationHandler implements Runnable{

    private AuctionHandler handler;

    public AuctionTerminationHandler(AuctionHandler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            this.handler.out.writeObject("ENDAUC");
            this.handler.terminateFlag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
