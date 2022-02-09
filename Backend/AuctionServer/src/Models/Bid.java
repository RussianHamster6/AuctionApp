package Models;

import java.io.Serializable;

public class Bid implements Serializable {
    public String bidder;
    public float amount;

    public Bid (String bidder, float amount){
        this.bidder = bidder;
        this.amount = amount;
    }

    public String stringifyBid(){
        return this.bidder + ": " + String.valueOf(this.amount);
    }
}
