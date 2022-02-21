package ConnectionHandlers;

import Controllers.AuctionController;
import Controllers.AuctionMenuController;
import Models.Auction;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class AuctionMenuConnection implements Runnable{
    private Socket server;
    private ObjectInputStream in;
    private AuctionMenuController auctionMenuController;

    public AuctionMenuConnection(Socket s, AuctionMenuController AMC){
        this.server = s;
        this.auctionMenuController = AMC;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Auction serverResponse = null;
                String strServerResponse = null;

                Object input = in.readObject();
                var x = input.getClass();

                if(x.isInstance(new ArrayList<Auction>())){
                    //Expected response
                    strServerResponse = (String) input;
                    System.out.println(strServerResponse);
                    Platform.runLater(() -> {
                        auctionMenuController.alert("This auction has ended");
                    });
                }
                else{
                    Platform.runLater(() -> {
                        auctionMenuController.alert("Something went wrong");
                    });
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
