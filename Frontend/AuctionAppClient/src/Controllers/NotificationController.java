package Controllers;

import ConnectionHandlers.NotificationConnection;
import Models.Auction;
import Models.AuctionConnectionDetails;
import Models.AuctionTableRow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotificationController extends Controller implements Initializable {

    String auctionLogText;
    @FXML
    javafx.scene.control.TextArea notificationText;

    private ObjectOutputStream out;
    private ArrayList<AuctionTableRow> auctionTableRows;
    private ArrayList<Auction> followedAuctions;

    private String hostName;
    private int portNumber;

    private Socket socket;
    private NotificationConnection notificationConnection;

    //Constructor
    public NotificationController(ArrayList<AuctionTableRow> inAuc){
        auctionLogText = new String();
        auctionTableRows = inAuc;
        //followedAuctions = inAuc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Do Connectiony Stuff
        hostName = "127.0.0.1";
        portNumber = 9090;

        try {
            socket = new Socket(hostName, portNumber);
            notificationConnection = new NotificationConnection(socket, this);
            out = new ObjectOutputStream(socket.getOutputStream());

            new Thread(notificationConnection).start();
            out.writeObject(auctionTableRows);

        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (IOException e) {
            System.err.println("Issue with Socket connection");
            e.printStackTrace();
        }
    }

    public void updateNotificationText(Auction auction){
        Boolean isFollowed = false;
        for (AuctionTableRow auc : this.auctionTableRows){
            if(auc.getItemName().equals(auction.itemName)){
                isFollowed = true;
            }
        }
        if(isFollowed){
            this.auctionLogText = this.auctionLogText +"\n" + auction.itemName + "- "  + auction.currentHighBid.bidder + ": " + String.valueOf(auction.currentHighBid.amount);
            notificationText.setText(this.auctionLogText);
        }
    }

    public void populateNotificationText(ArrayList<Auction> auctions){
        for(Auction auction : auctions){
            this.auctionLogText = this.auctionLogText + "\n" + auction.itemName + " Current High Bid - " + auction.currentHighBid.bidder + ": " + String.valueOf(auction.currentHighBid.amount);
        }
        notificationText.setText(this.auctionLogText);
    }

    public void closeNotifications() throws IOException {
        out.writeObject(new AuctionConnectionDetails("notificationGoingBack"));
        Stage stage = (Stage) notificationText.getScene().getWindow();
        stage.close();
    }

}
