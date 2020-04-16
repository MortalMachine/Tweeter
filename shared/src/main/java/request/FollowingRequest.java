package request;

import java.io.Serializable;

import model.domain.User;

public class FollowingRequest {

    public User follower;
    public int limit;
    public User lastFollowee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowingRequest() {}

    public FollowingRequest(User follower, int limit, User lastFollowee) {
        this.follower = follower;
        this.limit = limit;
        this.lastFollowee = lastFollowee;
    }

    public User getFollower() {
        return follower;
    }

    public int getLimit() {
        return limit;
    }

    public User getLastFollowee() {
        return lastFollowee;
    }
}
