package ConnectionHandlers;

import Controllers.AuctionController;
import Models.Auction;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class AuctionConnection implements Runnable {
    private Socket server;
    private ObjectInputStream in;
    private AuctionController auctionController;
    public Auction auction;

    public AuctionConnection(Socket s, AuctionController controller) throws IOException {
        server = s;
        in = new ObjectInputStream(server.getInputStream());
        this.auctionController = controller;
    }

    @Override
    public void run() {
            try {
                while(true) {
                    Auction serverResponse = null;
                    String strServerResponse = null;

                    Object input = in.readObject();

                    if(input == null) {
                        break;
                    }

                    var x = input.getClass();

                    if(x.isInstance("string")){
                        strServerResponse = (String) input;
                        System.out.println(strServerResponse);
                        Platform.runLater(() -> {
                            auctionController.alert("This auction has ended");
                            try {
                                auctionController.BackButtonPress();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else{
                        serverResponse = (Auction) input;
                        if(serverResponse == null) break;

                        this.auctionController.updateAucFields(serverResponse);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
}
