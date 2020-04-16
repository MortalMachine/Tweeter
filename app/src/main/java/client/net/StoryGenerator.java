package client.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.domain.Status;
import model.domain.Story;
import model.domain.User;

public class StoryGenerator {
    private final int MAX_STATUSES_ALLOWED = 100;
    private final int MIN_STATUSES_ALLOWED = 0;
    private static StoryGenerator storyGenerator;

    private StoryGenerator() {}
    public static StoryGenerator getInstance() {
        if (storyGenerator == null) {
            storyGenerator = new StoryGenerator();
        }
        return storyGenerator;
    }

    public enum Sort {
        NEWEST_TO_OLDEST
    }

    public Story generateStoryForUser(int minStatusesPerUser, int maxStatusesPerUser, User user, Sort sortOrder) throws Exception {

        //assert minStatusesPerUser >= MIN_STATUSES_ALLOWED : minStatusesPerUser;
        //assert maxStatusesPerUser < MAX_STATUSES_ALLOWED : maxStatusesPerUser;
        if (minStatusesPerUser < MIN_STATUSES_ALLOWED) {
            minStatusesPerUser = MIN_STATUSES_ALLOWED;
        }
        if (maxStatusesPerUser > MAX_STATUSES_ALLOWED) {
            maxStatusesPerUser = MAX_STATUSES_ALLOWED;
        }

        // For each user, generate a random number of followers between the specified min and max
        Random random = new Random();
        int numbStatusesToGenerate = random.nextInt(
                    maxStatusesPerUser - minStatusesPerUser) + minStatusesPerUser;

        List<Status> selectedStatuses = new ArrayList<>();
        selectedStatuses = StatusGenerator.getInstance().generateStatuses(numbStatusesToGenerate);

/*
        switch (sortOrder) {
            case NEWEST_TO_OLDEST:
                Collections.sort(selectedStatuses, new Comparator<Status>() {
                    @Override
                    public int compare(Status status1, Status status2) {
                        boolean result = status1.getMilliseconds().before(
                                status2.getMilliseconds());

                        if(result) {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    }
                });
                break;
            default:
                // It should be impossible to get here
                assert false;
        }
*/
        switch (sortOrder) {
            case NEWEST_TO_OLDEST:
                Collections.sort(selectedStatuses, new Comparator<Status>() {
                    @Override
                    public int compare(Status status1, Status status2) {
                        int result = status1.compareTo(status2);

                        return result;
                    }
                });
                break;
            default:
                // It should be impossible to get here
                assert false;
        }

        Story story = new Story();
        story.setStory(selectedStatuses);
        return story;
    }
}
