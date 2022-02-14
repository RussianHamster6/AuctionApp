package Repository;

import Models.Auction;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuctionRepositoryMock {

    @Mock
    AuctionRepository mockedRepository;
    ArrayList<Auction> aucList = new ArrayList<Auction>();


    public AuctionRepositoryMock(){
        aucList.add(new Auction("Temp", 1, 10,1));
        aucList.add(new Auction("Auction for Stuff",10,100,5));
        //GetAuctionByName mocks
        when(mockedRepository.getAuctionByName("Temp")).thenReturn(aucList.get(0));
        when(mockedRepository.getAuctionByName(("Auction for Stuff"))).thenReturn(aucList.get(1));
        //GetAllAuctionMock
        when(mockedRepository.getAllAuctions()).thenReturn(aucList);
    }
}
