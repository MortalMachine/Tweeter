package response;

import model.domain.User;

public class GetUserResponse extends Response {
    private User user;

    public GetUserResponse(boolean success, String message, User user) {
        super(success, message);
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
