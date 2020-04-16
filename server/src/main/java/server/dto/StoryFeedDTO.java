package server.dto;

import java.util.List;

import model.domain.Status;

public class StoryFeedDTO {
    private List<Status> statuses;
    private boolean hasMorePages;
    public StoryFeedDTO(List<Status> statuses, boolean hasMorePages) {
        this.statuses = statuses;
        this.hasMorePages = hasMorePages;
    }
    public List<Status> getStatuses() {
        return statuses;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }
}
