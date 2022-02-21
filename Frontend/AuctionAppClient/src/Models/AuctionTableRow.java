package Models;

public class AuctionTableRow {
    public String itemName;
    public float startingBid;
    public float biddingIncrement;
    public boolean favourite;

    public AuctionTableRow(String IN, float SB, float BI){
        this.itemName = IN;
        this.startingBid = SB;
        this.biddingIncrement = BI;
        this.favourite = false;
    }
}
