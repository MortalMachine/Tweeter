package server.lambda.logout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.LogoutRequest;
import response.LogoutResponse;
import server.service.LogoutServiceImpl;

/*
{
  "user": {
    "firstName": "Mickey",
    "lastName": "Mouse",
    "alias": "@themouse",
    "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  }
}
*/
public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context) {
        LogoutServiceImpl service = new LogoutServiceImpl();
        return service.logout(request);
    }
}
