package server.dao.follows;

import java.util.List;

import server.dto.FollowsDTO;
import model.domain.User;

public interface IFollowsDAO {
    void putFollowee(String alias, String firstname);
    void deleteFollowee(String alias, String firstname);
    List<User> getAllFollowers(String followee_alias);
    FollowsDTO getFollowers(String followee_alias, User lastFollower);
    FollowsDTO getFollowees(String follower_alias, User lastFollowee);
}
