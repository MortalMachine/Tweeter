package server.dao.unfollow;

import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;

public class UnfollowDummyDAO implements IUnfollowDAO {
    @Override
    public UnfollowPersonResponse postUnfollow(UnfollowPersonRequest request) {
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
        return new UnfollowPersonResponse(request.getAuthToken());
    }
}
