package Models;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

public class AuctionTableRow {
    private String itemName;
    private float startingBid;
    private float biddingIncrement;
    private boolean favourite;

    public AuctionTableRow(String IN, float SB, float BI){
        this.itemName = IN;
        this.startingBid = SB;
        this.biddingIncrement = BI;
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
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
