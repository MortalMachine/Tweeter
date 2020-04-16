package server.lambda.post_status;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.PostStatusRequest;
import response.PostStatusResponse;
import server.service.PostStatusServiceImpl;
/*
{
  "status": {
      "message": "Don't hoard toilet paper you maniac.",
      "milliseconds": 7200000,
      "mentions": []
  },
  "authToken": {
    "TWO_HRS_IN_MS": 7200000,
    "authToken": "$input.params('AuthToken')",
    "alias": "$input.params('Username')",
    "expMs": "$input.params('Expiration')"
  }
}

{
  "status": {
      "message": "Don't hoard toilet paper you maniac.",
      "milliseconds": 7200000,
      "mentions": []
  },
  "authToken": {
    "TWO_HRS_IN_MS": 7200000,
    "id": "Hardcoded AuthToken",
    "alias": "the_mouse",
    "expMs": 7200000
  }
}
{
  "status": {
    "message": "Don't hoard toilet paper you maniac.",
    "milliseconds": 7200000,
    "mentions": []
  },
  "authToken": {
    "id": "dc9937f7-893d-48a8-b0b9-5230471071d3",
    "alias": "the_mouse",
    "expMs": 7200000
  }
}
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        return new PostStatusServiceImpl().postStatus(request);
    }
}
