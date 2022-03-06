import Handlers.AuctionHandler;
import Helpers.AuctionHandlerHelpers;
import Models.Auction;
import Models.AuctionTableRow;
import Models.Login;
import Repository.AuctionRepository;
import Repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AuctionHandlerTests {
    ArrayList<AuctionHandler> auctionHandlerArrayList;
    AuctionRepository auctionRepository;
    ArrayList<Auction> aucList;
    ArrayList<Login> loginList;
    ArrayList<Login> hashedLoginList;
    UserRepository userRepository;
    AuctionHandler auctionHandler;
    private static ExecutorService pool;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {

        auctionHandlerArrayList = new ArrayList<>();
        //duplicated ConfigureMocks method from Main
        auctionRepository = Mockito.mock(AuctionRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        aucList = new ArrayList<Auction>();
        loginList = new ArrayList<Login>();
        hashedLoginList = new ArrayList<>();
        pool = Executors.newFixedThreadPool(50);

        //Add mock auctions to aucList
        aucList.add(new Auction("Temp", 1, 10,1));
        aucList.add(new Auction("Auction for Stuff",10,100,5));
        aucList.add(new Auction("Ended Auction", 1, 0, -1));
        //GetAuctionByName mocks
        for(Auction auc: aucList){
            Mockito.when(auctionRepository.getAuctionByName(auc.itemName)).thenReturn(auc);
        }
        //GetAllAuctionMock
        Mockito.when(auctionRepository.getAllAuctions()).thenReturn(aucList);
        //Mock Users
        loginList.add(new Login("user", "pass"));
        loginList.add(new Login("secondUser", "pass2"));
        //GetUserByName mocks
        for(Login login: loginList){
            //Hash password
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(login.password.getBytes());
            String passwordHashed = new String(messageDigest.digest());

            Login newLogin = new Login(login.userName, passwordHashed);
            hashedLoginList.add(newLogin);
            Mockito.when(userRepository.getUserByUsername(login.userName)).thenReturn(newLogin);
        }
        //Mockito.when(userRepository.getUserByUsername(anyString())).thenReturn(null);

        this.auctionHandler = new AuctionHandler(new Socket(), auctionHandlerArrayList, auctionRepository, userRepository);
        auctionHandlerArrayList.add(auctionHandler);
    }

    @Test
    public void ifValidLoginWithValidDetailsThenTrue(){
        var result = AuctionHandlerHelpers.ValidLogin(hashedLoginList.get(0), auctionHandlerArrayList, auctionHandler);

        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }

    @Test
    public void ifValidLoginWithInValidDetailsThenFalse(){
        var result = AuctionHandlerHelpers.ValidLogin(new Login("INVALID", "INVALID"), auctionHandlerArrayList, auctionHandler);

        Assert.assertNotNull(result);
        Assert.assertFalse(result);
    }

    @Test
    public void GetAuctionsFromTableRowsWithValidDataThenCorrectAuctionsReturned(){
        ArrayList<AuctionTableRow> ATRList = new ArrayList<>();
        Auction auctionToAdd = aucList.get(0);
        ATRList.add(new AuctionTableRow(auctionToAdd.itemName,auctionToAdd.auctionHistory.get(0).amount, auctionToAdd.bidIncrement,auctionToAdd.auctionEndDateTime));
        var result = AuctionHandlerHelpers.GetAuctionsFromTableRows(ATRList, auctionRepository);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.get(0) == auctionToAdd);
    }

    @Test
    public void GetAuctionsFromTableRowsWithInvalidDataThenNullReturned(){
        ArrayList<AuctionTableRow> ATRList = new ArrayList<>();
        Auction auctionToAdd = new Auction("Invalid",1,1,1);
        ATRList.add(new AuctionTableRow(auctionToAdd.itemName,auctionToAdd.auctionHistory.get(0).amount, auctionToAdd.bidIncrement,auctionToAdd.auctionEndDateTime));
        var result = AuctionHandlerHelpers.GetAuctionsFromTableRows(ATRList, auctionRepository);

        Assert.assertNull(result.get(0));
    }
}
