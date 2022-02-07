package Threads;

import java.io.*;
import java.net.Socket;

public class AutctionThread extends Thread {

    private Socket socket = null;

    public AutctionThread(Socket socket){
        super("AuctionThread");

        this.socket = socket;
    }

    public void run(){
        try (
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        )
        {
            System.out.println("Auction Started :D");
            while (true){
                String inputLine = in.readLine();

                //new auction input
                if(inputLine != null){

                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
