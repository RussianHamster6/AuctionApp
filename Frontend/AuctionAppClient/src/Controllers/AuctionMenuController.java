package Controllers;

import ConnectionHandlers.AuctionMenuConnection;
import Models.Auction;
import Models.AuctionConnectionDetails;
import Models.AuctionTableRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AuctionMenuController extends Controller implements Initializable {

    @FXML
    public TableView<AuctionTableRow> AuctionTable;

    @FXML
    TableColumn<AuctionTableRow, String> itemNameCol;
    @FXML
    TableColumn<AuctionTableRow, Float> startingBidCol;
    @FXML
    TableColumn<AuctionTableRow, Float> biddingIncrementCol;
    @FXML
    TableColumn<AuctionTableRow, Boolean> favCol;
    @FXML
    TableColumn<AuctionTableRow, String> endTimeCol;
    @FXML
    TextField searchText;

    public ObservableList<AuctionTableRow> ATRList;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
    private Boolean filterFav = false;
    private Boolean filterDateTime = false;

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
            endTimeCol.setCellValueFactory(new PropertyValueFactory<>("auctionEndTime"));
            endTimeCol.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAuctionEndTime().format(formatter)));

            new Thread(aucConn).start();
            out.writeObject(new AuctionConnectionDetails("GETALLAUC"));
        } catch (UnknownHostException e){
            System.err.println("Unknown Host");
        } catch (IOException e) {
            System.err.println("Issue with Socket connection");
            e.printStackTrace();
        }

        //set listener on searchtext
        searchText.textProperty().addListener((observable,oldvalue,newvalue) -> {
            searchItem(newvalue);
        });
    }

    public void populateTable(ArrayList<Auction> auctionArrayList){
        for (Auction a : auctionArrayList){
            var toAdd = new AuctionTableRow(a.itemName,a.auctionHistory.get(0).amount,a.bidIncrement, a.auctionEndDateTime);
            AuctionTable.getItems().add(toAdd);
        }
    }

    public void viewAuction(MouseEvent event) throws IOException{
        if(AuctionTable.getSelectionModel().getSelectedItem() != null) {
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

    public void setFavourite(){
        if(AuctionTable.getSelectionModel().getSelectedItem() != null) {
            var selectedVal = AuctionTable.getSelectionModel().getSelectedItem();
            var newItem = selectedVal;
            var tableItems = ATRList;
            if (selectedVal.isFavourite()) {
                newItem.setFavourite(false);
            } else {
                newItem.setFavourite(true);
            }
            tableItems.set(tableItems.indexOf(selectedVal), newItem);
            ATRList = tableItems;
            AuctionTable.setItems(ATRList);
        }
    }

    public void Deselect(){
        AuctionTable.getSelectionModel().clearSelection();
    }

    public void searchItem(String searchString) {
        FilteredList<AuctionTableRow> AFiltered = new FilteredList<>(ATRList);
        if (searchString != null) {
            Predicate<AuctionTableRow> pred = auctionTableRow -> {
                if(auctionTableRow.getItemName().toLowerCase().contains(searchString.toLowerCase())){
                    return true;
                }
                return false;
            };
            AFiltered.setPredicate(pred);
        } else {
            AuctionTable.setItems(ATRList);
        }

        AuctionTable.setItems(AFiltered);
    }

    public void filterFavourite(){
        FilteredList<AuctionTableRow> AFiltered = new FilteredList<>(ATRList);

        if(!filterFav){
            filterFav = true;

            Predicate<AuctionTableRow> pred = auctionTableRow -> auctionTableRow.isFavourite();

            AFiltered.setPredicate(pred);
        }
        else{
            filterFav = false;
            AuctionTable.setItems(ATRList);
        }

        AuctionTable.setItems(AFiltered);
    }

    public void filterCompleteAuctions(){
        FilteredList<AuctionTableRow> AFiltered = new FilteredList<>(ATRList);

        if(!filterDateTime){
            filterDateTime = true;
            LocalDateTime now = LocalDateTime.now();

            Predicate<AuctionTableRow> pred = auctionTableRow -> {
                if(auctionTableRow.getAuctionEndTime().isBefore(now)){
                    return false;
                }
                else{
                    return true;
                }
            };

            AFiltered.setPredicate(pred);
        }
        else{
            filterDateTime = false;
            AuctionTable.setItems(ATRList);
        }

        AuctionTable.setItems(AFiltered);
    }
}
