package ConnectionHandlers;

import Controllers.AuctionController;
import Models.Auction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                    serverResponse = (Auction) in.readObject();

                    if(serverResponse == null) break;

                    System.out.println("Server Response: ");
                    System.out.println(serverResponse.itemName);
                    this.auctionController.updateAucFields(serverResponse);

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
