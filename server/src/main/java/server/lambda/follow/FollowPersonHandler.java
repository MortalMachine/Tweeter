package server.lambda.follow;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.FollowPersonRequest;
import response.FollowPersonResponse;
import server.service.FollowPersonServiceImpl;

/*
{
  "person": {
      "firstName": "Donald",
      "lastName": "Duck",
      "alias": "the_duck",
      "imageUrl": "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"
  },
  "authToken": {
    "TWO_HRS_IN_MS": 7200000,
    "authToken": "Hardcoded AuthToken",
    "alias": "the_mouse",
    "expMs": 7200000
  }
}
 */
public class FollowPersonHandler implements RequestHandler<FollowPersonRequest, FollowPersonResponse> {
    @Override
    public FollowPersonResponse handleRequest(FollowPersonRequest request, Context context) {
        FollowPersonServiceImpl service = new FollowPersonServiceImpl();
        return service.postFollow(request);
    }
}
