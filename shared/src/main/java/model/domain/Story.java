package model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Story implements Serializable {
    private List<Status> story;
    private List<Status> pageStory;

    public Story() {
        story = new ArrayList<>();
    }
    public void addStatus(Status status) {
        story.add(0, status);
    }
    public void setStory(List<Status> story) { this.story = story; }
    public void setPageStory(List<Status> statuses) { pageStory = statuses; }
    public List<Status> getStory() {
        return story;
    }
    public List<Status> getPageStory() { return pageStory; }
}
