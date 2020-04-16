package response;

import model.domain.AuthToken;

public class UnfollowPersonResponse extends Response {
    private AuthToken authToken;

    public UnfollowPersonResponse(boolean success, String message) {
        super(success, message);
        authToken = null;
    }
    public UnfollowPersonResponse(AuthToken authToken) {
        super(true, null);
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
