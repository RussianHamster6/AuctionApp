import Main.Main;
import Models.Auction;
import Models.Login;
import Repository.AuctionRepository;
import Repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;

public class RepositoryTests {
    AuctionRepository auctionRepository;
    UserRepository userRepository;
    ArrayList<Auction> aucList;
    ArrayList<Login> loginList;

    @Before
    public void init() throws NoSuchAlgorithmException {
        //duplicated ConfigureMocks method from Main
        auctionRepository = Mockito.mock(AuctionRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        aucList = new ArrayList<Auction>();
        loginList = new ArrayList<Login>();

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
        Mockito.when(userRepository.getUserByUsername(anyString())).thenReturn(null);
        for(Login login: loginList){
            //Hash password
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(login.password.getBytes());
            String passwordHashed = new String(messageDigest.digest());

            Login newLogin = new Login(login.userName, passwordHashed);

            Mockito.when(userRepository.getUserByUsername(login.userName)).thenReturn(newLogin);
        }
    }

    @Test
    public void getAllAuctionsThenValidResult(){
        var result = auctionRepository.getAllAuctions();

        Assert.assertNotNull(result);
        Assert.assertTrue(result == aucList);
    }

    @Test
    public void getSpecificAuctionWithValidNameThenValidResult(){
        var result = auctionRepository.getAuctionByName("Temp");

        Assert.assertNotNull(result);
        Assert.assertTrue(result == aucList.get(0));
    }

    @Test
    public void getSpecificAuctionWithInvalidNameThenNull(){
        var result = auctionRepository.getAuctionByName("INVALID");

        Assert.assertNull(result);
    }

    @Test
    public void getSpecificLoginWithValidNameThenValidResult(){
        var result = userRepository.getUserByUsername("user");

        Assert.assertNotNull(result);
        Assert.assertTrue(result.userName == loginList.get(0).userName);
    }
}
