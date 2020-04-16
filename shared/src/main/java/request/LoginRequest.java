package request;

import java.io.Serializable;

import model.domain.User;

public class LoginRequest implements Serializable {
    public User user;
    public String password;

    private LoginRequest() {}

    public LoginRequest(User user, String password) {
        this.user = user;
        this.password = password;
    }
    public User getUser() { return user; }
    public String getPassword() { return password; }
}
