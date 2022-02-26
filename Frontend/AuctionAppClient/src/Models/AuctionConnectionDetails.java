package Models;

import java.io.Serializable;

public class AuctionConnectionDetails implements Serializable {
    public String action;

    public AuctionConnectionDetails(String A){
        this.action = A;
    }
}
