package ConnectionHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AuctionConnection implements Runnable {
    private Socket server;
    private BufferedReader in;

    public AuctionConnection(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    @Override
    public void run() {
            try {
                while(true) {
                    String serverResponse = in.readLine();

                    if(serverResponse == null) break;

                    System.out.println(serverResponse);
                }
            } catch (IOException e) {
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
