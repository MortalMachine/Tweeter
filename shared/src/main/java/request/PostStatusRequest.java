package request;

import model.domain.AuthToken;
import model.domain.Status;

public class PostStatusRequest {
    public Status status;
    public AuthToken authToken;

    private PostStatusRequest() {}
    public PostStatusRequest(Status status, AuthToken authToken) {
        this.status = status;
        this.authToken = authToken;
    }
    public Status getStatus() {
        return status;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
