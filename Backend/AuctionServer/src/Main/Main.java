package Main;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException{

        //Ensures that the solution has been started with the correct CLI arguments
        if (args.length != 1){
            System.err.println("Usage: java SocketTestServer <port number>");
            System.exit(1);
        }

        int portNum = Integer.parseInt(args[0]);

        System.out.println("Hello World");
    }
}
