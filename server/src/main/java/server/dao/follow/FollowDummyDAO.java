package server.dao.follow;

import request.FollowPersonRequest;
import response.FollowPersonResponse;

public class FollowDummyDAO implements IFollowDAO {
    @Override
    public FollowPersonResponse postFollow(FollowPersonRequest request) throws NullPointerException {
        if (request.getPerson() == null ||
            request.getPerson().getAlias() == null ||
            request.getAuthToken() == null ||
            request.getAuthToken().getId() == null ||
            request.getAuthToken().getAlias() == null) {
            throw new NullPointerException();
        }
        else if (System.currentTimeMillis() > request.getAuthToken().getExpMs()) {
            request.getAuthToken().setExpMs();
        }
        return new FollowPersonResponse(true, request.getAuthToken(), null);
    }
}
