package Models;

import java.io.Serializable;

public class Login implements Serializable {
    public String userName;
    public String password;

    public Login(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
