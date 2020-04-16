package server.lambda.unfollow;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;
import server.service.UnfollowPersonServiceImpl;
/*
{
  "person": {
      "firstName": "Donald",
      "lastName": "Duck",
      "alias": "theduck",
      "imageUrl": "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"
  },
  "authToken": {
    "TWO_HRS_IN_MS": 7200000,
    "authToken": "Hardcoded AuthToken",
    "alias": "themouse",
    "expMs": 7200000
  }
}
 */

public class UnfollowPersonHandler implements RequestHandler<UnfollowPersonRequest, UnfollowPersonResponse> {
    @Override
    public UnfollowPersonResponse handleRequest(UnfollowPersonRequest request, Context context) {
        UnfollowPersonServiceImpl service = new UnfollowPersonServiceImpl();
        return service.postUnfollow(request);
    }
}
