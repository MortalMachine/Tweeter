package response;

import model.domain.AuthToken;
import model.domain.User;

public class RegisterResponse extends Response {
    private User user;
    private AuthToken token;

    public RegisterResponse(boolean success, String message, User user, AuthToken token) {
        super(success, message);
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }
    public AuthToken getToken() {
        return token;
    }
}
