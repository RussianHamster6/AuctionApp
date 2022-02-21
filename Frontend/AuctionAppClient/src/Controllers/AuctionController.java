package Controllers;

import ConnectionHandlers.AuctionConnection;
import Models.Auction;
import Models.Bid;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    public Auction auction;

    public void BidButtonPress() throws IOException {
        Bid newBid = new Bid("ClientId", auction.currentHighBid.amount + auction.bidIncrement);
        out.writeObject(newBid);
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
            out.writeObject("Auction for Stuff");

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
