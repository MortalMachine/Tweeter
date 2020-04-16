package response;

import java.util.List;

import model.domain.Status;

public class FeedResponse extends PagedResponse {
    private List<Status> statuses;

    public FeedResponse(String message) {
        super(false, message, false);
    }

    public FeedResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    public List<Status> getStatuses() {
        return statuses;
    }
}
