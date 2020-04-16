package server.lambda.register;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.RegisterRequest;
import response.RegisterResponse;
import server.service.RegisterServiceImpl;

/*
{
  "user": {
    "firstName": "fname10001",
    "lastName": "lname10001",
    "alias": "tempAlias10001",
    "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  },
  "password": "password"
}
{
  "user": {
    "firstName": "fname10001",
    "lastName": "lname10001",
    "alias": "tempAlias10001",
    "imageUrl": "/home/jordan/Pictures/Mickey-mouse-head-clipart-free-images.gif"
  },
  "password": "password"
}
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest request, Context context) {
        RegisterServiceImpl service = new RegisterServiceImpl();
        return service.register(request);
    }
}
