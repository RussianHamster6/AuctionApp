package Repository;
import Models.Auction;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        /*try {
            Auction matchedAuction = mockAuctions.stream()
                    .filter(a -> searchItem.equals(a.itemName))
                    .findAny()
                    .orElse(null);

            if (matchedAuction != null) {
                return matchedAuction;
            } else {
                throw new Exception("Auction could not be found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }*/
    }

    public void updateAuction(String searchItem, Auction auction){
        /*try{
            Auction matchedAuction = mockAuctions.stream()
                    .filter(a -> searchItem.equals(a.itemName))
                    .findAny()
                    .orElse(null);

            if (matchedAuction != null) {
                mockAuctions.set(mockAuctions.indexOf(matchedAuction), matchedAuction);

            } else {
                throw new Exception("Auction could not be found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
