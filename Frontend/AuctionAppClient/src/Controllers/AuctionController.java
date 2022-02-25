package Controllers;

import ConnectionHandlers.AuctionConnection;
import Models.Auction;
import Models.Bid;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class AuctionController extends Controller implements Initializable {

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

    public AuctionController(String aucName){
        this.auctionName = aucName;
    }

    public void BidButtonPress() throws IOException {
        Bid newBid = new Bid("ClientId", auction.currentHighBid.amount + auction.bidIncrement);
        out.writeObject(newBid);
    }

    public void BackButtonPress() throws IOException {
        out.writeObject("GOINGBACK");
        Stage stage = (Stage) highBidText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/auctionMenu.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/auctionMenu.fxml"));
        stage.setTitle("Auction App");
        stage.setScene(new Scene(root));
        stage.show();
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
        this.auction = auc;

        Task task = new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                highBidText.setText(String.valueOf(auction.currentHighBid.amount));
                bidIncrementText.setText(String.valueOf(auction.bidIncrement));
                itemNameText.setText(auction.itemName);

                String auctionLogtext = new String();
                for(Bid bid : auction.auctionHistory){
                    auctionLogtext = auctionLogtext + "\n" + bid.bidder + ": " + String.valueOf(bid.amount);
                }
                auctionLog.setText(auctionLogtext);

                return null;
            };
        };

        new Thread(task).run();

    }
}
