package response;

import java.io.Serializable;
import java.util.List;

import model.domain.User;

public class FollowingResponse extends PagedResponse implements Serializable {

    public List<User> followees;

    public FollowingResponse(String message) {
        super(false, message, false);
    }

    public FollowingResponse(List<User> followees, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followees = followees;
    }

    public List<User> getFollowees() {
        return followees;
    }

    public FollowingResponse() {}

    public void setFollowees(List<User> followees) {
        this.followees = followees;
    }

}
