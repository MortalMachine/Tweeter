package server.service;

import model.domain.User;
import model.service.FeedService;
import request.FeedRequest;
import response.FeedResponse;
import server.dao.feed.FeedDynamoDAO;
import server.dao.feed.IFeedDAO;
import server.dto.StoryFeedDTO;

public class FeedServiceImpl implements FeedService {
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        User user = request.getUser();
        IFeedDAO feedDAO = new FeedDynamoDAO();

        /* Query user's feed */
        try {
            StoryFeedDTO sfDTO = feedDAO.getFeed(user.getAlias(), request.getLastStatus());

            /* Fill list of statuses for page */
            System.out.println("Sending feed response");
            return new FeedResponse(sfDTO.getStatuses(), sfDTO.hasMorePages());

        }
        catch (Exception e) {
            System.err.printf("Unable to read item: %s\n", user.getAlias());
            System.err.println(e.getMessage());
            return new FeedResponse(e.getMessage());
        }
    }
}
