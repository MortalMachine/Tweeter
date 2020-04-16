package client.presenter;

import java.io.IOException;

import request.LogoutRequest;
import response.LogoutResponse;
import response.Response;

public interface IPresenter {
    void update();
    void sendErrorMessage(Response response);
    LogoutResponse sendLogoutRequest(LogoutRequest request) throws IOException;
    void goToWelcomeFragment();
}
