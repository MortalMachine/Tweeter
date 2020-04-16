package server.lambda.following;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.FollowingRequest;
import response.FollowingResponse;
import server.service.FollowingServiceImpl;

/*
{
    "follower": {
        "firstName": "Mickey",
        "lastName": "Mouse",
        "alias": "@themouse",
        "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  },
    "limit" : 10,
    "lastFollowee": null
}
*/

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public FollowingResponse handleRequest(FollowingRequest request, Context context) {
        FollowingServiceImpl service = new FollowingServiceImpl();
        return service.getFollowees(request);
    }
}
