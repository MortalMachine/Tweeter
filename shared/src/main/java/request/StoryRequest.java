package request;

import java.io.Serializable;

import model.domain.Status;
import model.domain.User;

public class StoryRequest implements Serializable {
    public User user;
    public int limit;
    public Status lastStatus;

    private StoryRequest() {}

    public StoryRequest(User user, int limit, Status lastStatus) {
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public User getUser() {
        return user;
    }

    public int getLimit() {
        return limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }
}
