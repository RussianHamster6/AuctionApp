package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Auction implements Serializable {
    public String itemName;
    public Bid currentHighBid;
    public float bidIncrement;
    public ArrayList<Bid> auctionHistory;

    public Auction (String itemName, float bidIncrement, float startingPrice){
        this.itemName = itemName;
        this.bidIncrement = bidIncrement;
        this.auctionHistory = new ArrayList<Bid>();
        this.currentHighBid = new Bid("starting price", startingPrice);
        this.auctionHistory.add(this.currentHighBid);
    }

    //returns true if success, false if failed.
    public Boolean registerBid(Bid newBid){

        //If no current bid or bid is greater than currentHighBid amount then register bid
        if(currentHighBid == null || currentHighBid.amount < newBid.amount){
            currentHighBid = newBid;
            auctionHistory.add(newBid);
            return true;
        }
        else{
            return false;
        }
    }
}
