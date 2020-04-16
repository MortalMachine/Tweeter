package server.lambda.login;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import request.LoginRequest;
import response.LoginResponse;
import server.service.LoginServiceImpl;

/*
{
  "user": {
    "firstName": null,
    "lastName": null,
    "alias": "$input.params('Username')",
    "imageUrl": null
  },
  "password": "$input.params('Password')"
}
{
  "user": {
    "firstName": "Mickey",
    "lastName": "Mouse",
    "alias": "the_mouse",
    "imageUrl": "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
  },
  "password": "password"
}
{
  "user": {
    "firstName": "Mickey",
    "lastName": "Mouse",
    "alias": "the_mouse",
    "imageUrl": "/home/jordan/Pictures/Mickey-mouse-head-clipart-free-images.gif"
  },
  "password": "password"
}
 */
public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(LoginRequest request, Context context) {
        LoginServiceImpl service = new LoginServiceImpl();
        return service.login(request);
    }
}
