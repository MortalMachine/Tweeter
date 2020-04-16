package request;

import java.io.Serializable;

import model.domain.User;

public class RegisterRequest implements Serializable {
    public User user;
    public String password;

    private RegisterRequest() {}

    public RegisterRequest(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public User getUser() { return user; }
    public String getPassword() { return password; }
}
