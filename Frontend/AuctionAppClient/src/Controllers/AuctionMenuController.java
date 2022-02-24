package Controllers;

import ConnectionHandlers.AuctionConnection;
import ConnectionHandlers.AuctionMenuConnection;
import Models.Auction;
import Models.AuctionTableRow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionMenuController extends Controller implements Initializable {

    @FXML
    TableView<AuctionTableRow> AuctionTable;

    @FXML
    TableColumn<AuctionTableRow, String> itemNameCol;
    @FXML
    TableColumn<AuctionTableRow, Float> startingBidCol;
    @FXML
    TableColumn<AuctionTableRow, Float> biddingIncrementCol;
    @FXML
    TableColumn<AuctionTableRow, Boolean> favCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Do Connectiony Stuff
        String hostName = "127.0.0.1";
        int portNumber = 9090;

        try {
            Socket socket = new Socket(hostName, portNumber);
            AuctionMenuConnection aucConn = new AuctionMenuConnection(socket, this);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            //Set Column cell values
            itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            startingBidCol.setCellValueFactory(new PropertyValueFactory<>("startingBid"));
            biddingIncrementCol.setCellValueFactory(new PropertyValueFactory<>("biddingIncrement"));
            favCol.setCellValueFactory(new PropertyValueFactory<>("favourite"));

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
        for (Auction a : auctionArrayList){
            AuctionTable.getItems().add(new AuctionTableRow(a.itemName,a.auctionHistory.get(0).amount,a.bidIncrement));
        }
    }

    public void viewAuction(MouseEvent event) throws IOException{
        if (event.getClickCount() > 1) {

            Stage stage = (Stage) AuctionTable.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/auction.fxml"));
            AuctionController auctionController = new AuctionController(AuctionTable.getSelectionModel().getSelectedItem().getItemName());
            loader.setController(auctionController);
            Parent root = loader.load();

            stage.setScene(new Scene(root));
        }
    }
}
