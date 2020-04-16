package server.service;

import model.domain.User;
import model.service.StoryService;
import request.StoryRequest;
import response.StoryResponse;
import server.dao.story.IStoryDAO;
import server.dao.story.StoryDynamoDAO;
import server.dto.StoryFeedDTO;

public class StoryServiceImpl implements StoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {
        IStoryDAO storyDAO = new StoryDynamoDAO();
        User user = request.getUser();

        try {
            StoryFeedDTO sfDTO = storyDAO.getStory(user.getAlias(), request.getLastStatus());

            System.out.println("Sending story response");
            return new StoryResponse(sfDTO.getStatuses(), sfDTO.hasMorePages());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new StoryResponse(e.getMessage());
        }
    }
}
