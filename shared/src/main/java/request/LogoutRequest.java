package request;

import java.io.Serializable;

import model.domain.User;

public class LogoutRequest implements Serializable {
    public User user;

    private LogoutRequest() {}
    public LogoutRequest(User user) {
        this.user = user;
    }
    public User getUser() { return user; }
}
