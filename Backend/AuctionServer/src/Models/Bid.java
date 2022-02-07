package Models;

public class Bid {
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
