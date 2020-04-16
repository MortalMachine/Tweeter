package server.dao.unfollow;

import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;

public interface IUnfollowDAO {
    UnfollowPersonResponse postUnfollow(UnfollowPersonRequest request);
}
