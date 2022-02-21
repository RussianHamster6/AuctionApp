package Controllers;

import javafx.scene.control.Alert;

public class Controller {
    public void alert(String alertMessage){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(alertMessage);
        a.show();
    }

}
