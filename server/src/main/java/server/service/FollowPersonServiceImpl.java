package server.service;

import model.service.FollowPersonService;
import request.FollowPersonRequest;
import response.FollowPersonResponse;
import server.dao.follow.FollowDummyDAO;
import server.dao.follow.FollowDynamoDAO;
import server.dao.follow.IFollowDAO;

public class FollowPersonServiceImpl implements FollowPersonService {
    @Override
    public FollowPersonResponse postFollow(FollowPersonRequest request) throws NullPointerException {
        IFollowDAO followDAO = new FollowDummyDAO();
        FollowPersonResponse response = null;
        try {
            response = followDAO.postFollow(request);
        }
        catch (NullPointerException e) {
            String errorMsg = "Bad Request Error: " + e.getMessage();
            response = new FollowPersonResponse(false, errorMsg);
        }
        catch (Exception e) {
            String errorMsg = "Internal Server Error: " + e.getMessage();
            response = new FollowPersonResponse(false, errorMsg);
        }
        finally {
            return response;
        }
    }
}
