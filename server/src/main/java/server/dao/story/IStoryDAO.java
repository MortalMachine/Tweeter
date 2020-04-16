package server.dao.story;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import model.domain.Status;
import request.StoryRequest;
import response.StoryResponse;
import server.dto.StoryFeedDTO;

public interface IStoryDAO {
    StoryFeedDTO getStory(String alias, Status lastStatus);
}
