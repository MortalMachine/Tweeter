package server.dao.feed;

import java.util.List;

import model.domain.Status;
import model.domain.User;
import server.dto.FollowsDTO;
import server.dto.StoryFeedDTO;

public interface IFeedDAO {
    StoryFeedDTO getFeed(String alias, Status lastStatus);
    void putStatusInFollowersFeeds(List<User> followers, Status status);
}
