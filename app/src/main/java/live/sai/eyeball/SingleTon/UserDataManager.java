package live.sai.eyeball.SingleTon;

import live.sai.eyeball.Model.User;

public class UserDataManager {

    public static UserDataManager instance;

    private User user;
    private String username, email, id;

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setID(String id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}