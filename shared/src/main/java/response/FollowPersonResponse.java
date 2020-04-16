package response;

import model.domain.AuthToken;

public class FollowPersonResponse extends Response {
    private AuthToken authToken;

    public FollowPersonResponse(boolean success, AuthToken authToken, String message) {
        super(success, message);
        this.authToken = authToken;
    }
    public FollowPersonResponse(boolean success, String message) {
        super(success, message);
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
