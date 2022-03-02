package Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuctionTableRow implements Serializable {
    private String itemName;
    private float startingBid;
    private float biddingIncrement;
    private LocalDateTime auctionEndTime;
    private boolean favourite;

    public AuctionTableRow(String IN, float SB, float BI, LocalDateTime auctionEndTime){
        this.itemName = IN;
        this.startingBid = SB;
        this.biddingIncrement = BI;
        this.auctionEndTime = auctionEndTime;
        this.favourite = false;
    }

    //Getters
    public String getItemName(){
        return this.itemName;
    }
    public float getStartingBid(){
        return this.startingBid;
    }
    public float getBiddingIncrement(){
        return this.biddingIncrement;
    }
    public LocalDateTime getAuctionEndTime(){ return this.auctionEndTime;}
    public boolean isFavourite(){
        return this.favourite;
    }

    //Setters
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setStartingBid(float startingBid) {
        this.startingBid = startingBid;
    }
    public void setBiddingIncrement(float biddingIncrement){
        this.biddingIncrement = biddingIncrement;
    }
    public void setAuctionEndTime(LocalDateTime auctionEndTime) {this.auctionEndTime = auctionEndTime; }
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
