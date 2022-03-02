package ConnectionHandlers;

import Controllers.NotificationController;
import Models.Auction;
import Models.Bid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NotificationConnection implements Runnable{
    private Socket server;
    private ObjectInputStream in;
    private NotificationController notificationController;

    public NotificationConnection(Socket s, NotificationController NC) throws IOException {
        server = s;
        in = new ObjectInputStream(server.getInputStream());
        notificationController = NC;
    }

    @Override
    public void run() {
        try {
            while(true){
                ArrayList<Auction> allAuctions;

                Object input = in.readObject();

                if(input == null){
                    break;
                }

                var x = input.getClass();

                if(x.isInstance(new Auction("foo",1,1,1))){
                    this.notificationController.updateNotificationText((Auction) input);
                }
                else if(x.isInstance(new ArrayList<Auction>())){
                    this.notificationController.populateNotificationText((ArrayList<Auction>) input);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
