package Repository;
import Models.Auction;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuctionRepository {

    private List<Auction> mockAuctions = mock(List.class);

    public AuctionRepository(){
        mockAuctions.add(new Auction("Temp", 1, 10,1));
        mockAuctions.add(new Auction("Auction for Stuff",10,100,5));
    }

    public void addAuction(Auction auction){

        Auction matchedAuction = mockAuctions.stream()
                .filter(a -> auction.itemName.equals(a.itemName))
                .findAny()
                .orElse(null);

        if(matchedAuction == null){
            mockAuctions.add(auction);
        }
    }

    public List<Auction> getAllAuctions(){
        return this.mockAuctions;
    };

    public Auction getAuctionByName(String searchItem){
        try {
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
        }
    }

    public void updateAuction(String searchItem, Auction auction){
        try{
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
        }
    }
}
