package ConnectionHandlers;

import Controllers.AuctionMenuController;
import Models.Auction;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class AuctionMenuConnection implements Runnable{
    private Socket server;
    private ObjectInputStream in;
    private AuctionMenuController auctionMenuController;
    private Boolean runFlag = true;

    public AuctionMenuConnection(Socket s, AuctionMenuController AMC) throws IOException {
        this.server = s;
        in = new ObjectInputStream(server.getInputStream());
        this.auctionMenuController = AMC;
    }

    @Override
    public void run() {
        try {
            while(runFlag) {
                Object input = in.readObject();
                var x = input.getClass();

                if(x.isInstance(new ArrayList<Auction>())){
                    //Expected response
                    ArrayList<Auction> serverResponse = (ArrayList<Auction>) input;
                    Platform.runLater(() -> {
                        auctionMenuController.populateTable(serverResponse);
                    });
                    this.runFlag = false;
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
        }
    }


}
