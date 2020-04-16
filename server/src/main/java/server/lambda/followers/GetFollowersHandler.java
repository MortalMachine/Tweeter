package server.lambda.followers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.FollowersRequest;
import response.FollowersResponse;
import server.service.FollowerServiceImpl;

/*
{
    "followee": {
        "firstName": "Mickey",
        "lastName": "Mouse",
        "alias": "the_mouse",
        "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
    },
    "limit" : 10,
    "lastFollower": null
}
{
  "followee": {
    "firstName": "Mickey",
    "lastName": "Mouse",
    "alias": "the_mouse",
    "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  },
  "limit": 10,
  "lastFollower": {
    "firstName": "fname1005",
    "lastName": "lname1005",
    "alias": "@tempAlias1005"
  }
}
 */

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {
    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        FollowerServiceImpl service = new FollowerServiceImpl();
        return service.getFollowers(request);
    }
}
