package Controllers;

import ConnectionHandlers.AuctionConnection;
import ConnectionHandlers.AuctionMenuConnection;
import Models.Auction;
import Models.AuctionTableRow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionMenuController extends Controller implements Initializable {

    @FXML
    TableView AuctionTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Do Connectiony Stuff
        String hostName = "127.0.0.1";
        int portNumber = 9090;

        try {
            Socket socket = new Socket(hostName, portNumber);
            AuctionMenuConnection aucConn = new AuctionMenuConnection(socket, this);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            new Thread(aucConn).start();
            out.writeObject("GETALLAUC");

        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (IOException e) {
            System.err.println("Issue with Socket connection");
            e.printStackTrace();
        }
    }

    public void populateTable(ArrayList<Auction> auctionArrayList){
        ArrayList<AuctionTableRow> rowArrayList = new ArrayList<AuctionTableRow>();

        for (Auction a : auctionArrayList){
            rowArrayList.add(new AuctionTableRow(a.itemName,a.auctionHistory.get(0).amount,a.bidIncrement));
        }

        AuctionTable.setItems((ObservableList) rowArrayList.stream().toList());
    }
}
