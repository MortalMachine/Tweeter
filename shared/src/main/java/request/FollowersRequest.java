package request;

import java.io.Serializable;

import model.domain.User;

public class FollowersRequest implements Serializable {
    public User followee;
    public int limit;
    public User lastFollower;

    private FollowersRequest() {}

    public FollowersRequest(User followee, int limit, User lastFollower) {
        this.followee = followee;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }

    public User getFollowee() {
        return followee;
    }

    public int getLimit() {
        return limit;
    }

    public User getLastFollower() {
        return lastFollower;
    }
}
