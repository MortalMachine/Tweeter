package response;

import model.domain.AuthToken;
import model.domain.User;

public class LoginResponse extends Response {
    private User user;
    private AuthToken authToken;

    public LoginResponse(boolean success, String message, User user, AuthToken authToken) {
        super(success, message);
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
