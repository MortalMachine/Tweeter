package server.dao.follow;

import request.FollowPersonRequest;
import response.FollowPersonResponse;

public interface IFollowDAO {
    FollowPersonResponse postFollow(FollowPersonRequest request) throws NullPointerException;
}
