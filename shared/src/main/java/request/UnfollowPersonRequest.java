package request;

import model.domain.AuthToken;
import model.domain.User;

public class UnfollowPersonRequest {
    public User person;
    public AuthToken authToken;

    private UnfollowPersonRequest() {}
    public UnfollowPersonRequest(User person) {
        this.person = person;
    }
    public User getPerson() {
        return person;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
