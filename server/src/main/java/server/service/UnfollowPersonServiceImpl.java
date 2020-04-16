package server.service;

import model.service.UnfollowPersonService;
import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;
import server.dao.unfollow.IUnfollowDAO;
import server.dao.unfollow.UnfollowDummyDAO;

public class UnfollowPersonServiceImpl implements UnfollowPersonService {
    @Override
    public UnfollowPersonResponse postUnfollow(UnfollowPersonRequest request) {
        IUnfollowDAO followDAO = new UnfollowDummyDAO();
        UnfollowPersonResponse response = null;
        try {
            response = followDAO.postUnfollow(request);
        }
        catch (NullPointerException e) {
            String errorMsg = "Bad Request Error: " + e.getMessage();
            response = new UnfollowPersonResponse(false, errorMsg);
        }
        catch (Exception e) {
            String errorMsg = "Internal Server Error: " + e.getMessage();
            response = new UnfollowPersonResponse(false, errorMsg);
        }
        finally {
            return response;
        }
    }
}
