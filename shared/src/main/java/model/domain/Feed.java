package model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Feed implements Serializable {
    private List<Status> allStatuses;
    private List<Status> pageStatuses;

    public Feed() {
        allStatuses = new ArrayList<>();
        pageStatuses = new ArrayList<>();
    }
    public void addStatus(Status status) {
        allStatuses.add(status);
    }
    public void setAllStatuses(List<Status> feed) { this.allStatuses = feed; }
    public void setPageStatuses(List<Status> pageFeed) { this.pageStatuses = pageFeed; }
    public List<Status> getAllStatuses() {
        return allStatuses;
    }
    public List<Status> getPageStatuses() { return pageStatuses; }
}
