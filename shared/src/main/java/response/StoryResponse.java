package response;

import java.util.List;

import model.domain.Status;

public class StoryResponse extends PagedResponse {
    //private User user;
    private List<Status> statuses;

    public StoryResponse(String message) {
        super(false, message, false);
    }

    public StoryResponse(/*User user,*/ List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
/*
        this.user = user;
*/
        this.statuses = statuses;
    }

/*
    public User getUser() {
        return user;
    }
*/
    public List<Status> getStatuses() {
        return statuses;
    }
}
