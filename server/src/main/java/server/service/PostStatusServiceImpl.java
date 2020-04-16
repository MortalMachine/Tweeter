package server.service;

import java.util.List;

import model.domain.AuthToken;
import model.domain.User;
import model.service.PostStatusService;
import request.PostStatusRequest;
import response.PostStatusResponse;
import server.dao.auth.AuthDynamoDAO;
import server.dao.auth.IAuthDAO;
import server.dao.feed.FeedDynamoDAO;
import server.dao.feed.IFeedDAO;
import server.dao.follows.FollowsDynamoDAO;
import server.dao.follows.IFollowsDAO;
import server.dao.post_status.IPostStatusDAO;
import server.dao.post_status.PostStatusDummyDAO;
import server.dao.post_status.PostStatusDynamoDAO;

public class PostStatusServiceImpl implements PostStatusService {
    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        IAuthDAO authDAO = new AuthDynamoDAO();
        IPostStatusDAO postStatusDAO = new PostStatusDynamoDAO();
        IFollowsDAO followsDAO = new FollowsDynamoDAO();
        IFeedDAO feedDAO = new FeedDynamoDAO();
        try {
            /* Updates authtoken object from request if valid and expired */
            if (authDAO.isTokenValid(request.getAuthToken())) {
                postStatusDAO.postStatus(request);

                String followee_alias = request.getAuthToken().getAlias();
                List<User> allFollowers = followsDAO.getAllFollowers(followee_alias);
                feedDAO.putStatusInFollowersFeeds(allFollowers, request.getStatus());

                return new PostStatusResponse(true, null, request.getAuthToken());
            }
            else {
                return new PostStatusResponse(false, "Invalid authtoken", request.getAuthToken());
            }
        } catch (Exception e) {
            return new PostStatusResponse(false, e.getMessage(), request.getAuthToken());
        }
    }
}
