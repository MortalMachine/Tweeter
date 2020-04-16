package server.dao.story;

import java.util.ArrayList;
import java.util.List;

import model.domain.Status;
import request.StoryRequest;
import response.StoryResponse;
import server.lambda.StatusGenerator;

public class StoryDummyDAO /*implements IStoryDAO*/ {

    //@Override
    public StoryResponse getStory(StoryRequest request) {
        return new StoryResponse(
                StatusGenerator.getInstance().getNStatuses(request.getLimit()),
                true);
    }
}
