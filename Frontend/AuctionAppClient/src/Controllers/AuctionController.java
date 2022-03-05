package Controllers;

import ConnectionHandlers.AuctionConnection;
import Models.Auction;
import Models.AuctionConnectionDetails;
import Models.Bid;
import Models.Login;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class AuctionController extends Controller implements Initializable {

    private Login currentUser;
    @FXML
    public TextField highBidText;
    @FXML
    public TextField bidIncrementText;
    @FXML
    public Text itemNameText;
    @FXML
    public TextArea auctionLog;

    private ObjectOutputStream out;
    private String auctionName;
    public Auction auction;

    public AuctionController(String aucName, Login currentUser){
        this.auctionName = aucName;
        this.currentUser = currentUser;
    }

    public void BidButtonPress() throws IOException {
        Bid newBid = new Bid(this.currentUser.userName, auction.currentHighBid.amount + auction.bidIncrement);
        out.writeObject(newBid);
    }

    public void BackButtonPress() throws IOException {
        out.writeObject(new AuctionConnectionDetails("GOINGBACK"));
        Stage stage = (Stage) highBidText.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Do Connectiony Stuff
        String hostName = "127.0.0.1";
        int portNumber = 9090;

        try {
            Socket socket = new Socket(hostName, portNumber);
            AuctionConnection aucConn = new AuctionConnection(socket, this);
            out = new ObjectOutputStream(socket.getOutputStream());

            new Thread(aucConn).start();
            out.writeObject(auctionName);

        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (IOException e) {
            System.err.println("Issue with Socket connection");
            e.printStackTrace();
        }
    }

    public void updateAucFields(Auction auc){
        if(auc.itemName.equals(this.auctionName)) {
            this.auction = auc;

            Task task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    highBidText.setText(String.valueOf(auction.currentHighBid.amount));
                    bidIncrementText.setText(String.valueOf(auction.bidIncrement));
                    itemNameText.setText(auction.itemName);

                    String auctionLogtext = new String();
                    for (Bid bid : auction.auctionHistory) {
                        auctionLogtext = auctionLogtext + "\n" + bid.bidder + ": " + String.valueOf(bid.amount);
                    }
                    auctionLog.setText(auctionLogtext);

                    return null;
                }

                ;
            };

            new Thread(task).run();
        }
    }
}
