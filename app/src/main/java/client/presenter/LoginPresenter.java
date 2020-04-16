package client.presenter;

import java.io.IOException;
import java.io.Serializable;

import model.domain.User;
import client.model.service.GetUserService;
import client.model.service.LoginServiceProxy;
import client.view.IActivity;
import client.view.IView;
import request.LoginRequest;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.Response;

public class LoginPresenter implements IPresenter, Serializable {
    private IView loginView;

    public LoginPresenter(IView loginView) {
        this.loginView = loginView;
    }

    public LoginResponse sendLoginRequest(LoginRequest request) throws IOException {
        //request.getUser().addObserver(this);
        LoginResponse response = new LoginServiceProxy().login(request);
        return response;
    }

    public void update() {
        User user = GetUserService.getInstance().getUser().getUser();
        if (user == null) {
            loginView.displayToast("Alias or Password Not Found");
        }
        else {
            ((IActivity)loginView.getActivity()).goToHomeFragment(user);
        }
    }

    public void sendErrorMessage(Response response) {
        loginView.displayToast(response.getMessage());
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}

}
