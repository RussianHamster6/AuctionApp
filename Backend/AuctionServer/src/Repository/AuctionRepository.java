package Repository;
import Models.Auction;

import java.util.ArrayList;

//This class exists for me to mock from it. It has no functional purpose other than to be mocked.
public class AuctionRepository {

    public AuctionRepository(){
    }

    public ArrayList<Auction> getAllAuctions(){
        return new ArrayList<Auction>() {
        };
    };

    public Auction getAuctionByName(String searchItem){
        return new Auction("foo",1,1,1);
    }

    public void updateAuction(String searchItem, Auction auction){
    }
}
