package Repository;

import Models.Login;

public class UserRepository {

    public UserRepository(){
    }

    public Login getUserByUsername(String username){
        return new Login("foo","bar");
    }
}
