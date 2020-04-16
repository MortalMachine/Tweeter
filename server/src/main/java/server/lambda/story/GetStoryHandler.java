package server.lambda.story;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.StoryRequest;
import response.StoryResponse;
import server.service.StoryServiceImpl;

/*
{
  "user": {
    "firstName": "Mickey",
    "lastName": "Mouse",
    "alias": "the_mouse",
    "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  },
  "limit": 10,
  "lastStatus": null
}
 */

public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {
    @Override
    public StoryResponse handleRequest(StoryRequest request, Context context) {
        StoryServiceImpl service = new StoryServiceImpl();
        return service.getStory(request);
    }
}
