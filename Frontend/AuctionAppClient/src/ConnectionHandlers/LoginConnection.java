package ConnectionHandlers;

import Controllers.LoginController;
import Controllers.NotificationController;
import Models.Login;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class LoginConnection implements Runnable{
    private Socket server;
    private ObjectInputStream in;
    private LoginController loginController;
    private Boolean runFlag = true;

    public LoginConnection(Socket s, LoginController loginController) throws IOException {
        this.server = s;
        var serverInput = server.getInputStream();
        in = new ObjectInputStream(serverInput);
        this.loginController = loginController;
    }

    @Override
    public void run() {
        try{
            while(runFlag){
                Object input = in.readObject();
                var x = input.getClass();

                if(x.isInstance(true)){
                    runFlag = false;
                    //then it's good
                    Platform.runLater(() -> {
                        try {
                            this.loginController.loginResponse((Boolean) input);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }
                else{
                    runFlag = false;
                    Platform.runLater(() -> {
                        this.loginController.alert("Something went wrong, please try again later");
                    });
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
