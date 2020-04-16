package request;

import model.domain.AuthToken;
import model.domain.User;

public class FollowPersonRequest {
    public User person;
    public AuthToken authToken;

    private FollowPersonRequest() {}
    public FollowPersonRequest(User person, AuthToken authToken) {
        this.person = person;
        this.authToken = authToken;
    }
    public User getPerson() {
        return person;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
