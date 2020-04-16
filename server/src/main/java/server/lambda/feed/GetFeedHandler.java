package server.lambda.feed;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.FeedRequest;
import response.FeedResponse;
import server.service.FeedServiceImpl;
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

public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        return new FeedServiceImpl().getFeed(request);
    }
}
