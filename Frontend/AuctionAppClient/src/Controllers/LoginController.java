package Controllers;

import ConnectionHandlers.AuctionMenuConnection;
import ConnectionHandlers.LoginConnection;
import Models.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {

    @FXML
    TextField userNameText;
    @FXML
    TextField passwordText;

    private String hostName = "127.0.0.1";
    private int portNumber = 9090;
    private Socket socket;
    private ObjectOutputStream out;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket(hostName, portNumber);
            LoginConnection logCon = new LoginConnection(socket, this);
            out = new ObjectOutputStream(socket.getOutputStream());
            new Thread(logCon).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loginButtonPressed(){

        //Error handling
        if(userNameText.getText().isEmpty()){
            this.alert("Please input a username");
        }
        else if(passwordText.getText().isEmpty()){
            this.alert("Please input a password");
        }
        else {
            Login loginDetails = new Login(this.userNameText.getText(), this.passwordText.getText());

            //send login
            try{
                out.writeObject(loginDetails);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loginResponse(Boolean isValid) throws IOException {
        if (isValid){
            Stage primaryStage = (Stage) userNameText.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/auctionMenu.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/views/auctionMenu.fxml"));
            primaryStage.setTitle("Auction App");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        else{
            alert("Invalid Username/Password");
        }
    }
}
