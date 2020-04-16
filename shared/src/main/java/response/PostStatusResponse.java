package response;

import model.domain.AuthToken;

public class PostStatusResponse extends Response {
    private AuthToken authToken;
    public PostStatusResponse(boolean success, String message, AuthToken authToken) {
        super(success, message);
        this.authToken = authToken;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
