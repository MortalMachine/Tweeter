package client.presenter;

import java.io.IOException;
import java.io.Serializable;

import model.domain.User;
import client.model.service.LogoutServiceProxy;
import client.view.IActivity;
import client.view.home.HomeFragment;
import request.LogoutRequest;
import response.LogoutResponse;
import response.Response;

public class HomePresenter implements IPresenter, Serializable {

    private HomeFragment view;
    private User user;

    public HomePresenter(HomeFragment view) {
        this.view = view;
        user = view.getUser();
    }
/*
    public GetUserResponse getCurrentUser() {
        return GetUserService.getInstance().getUser();
    }
*/
    public LogoutResponse sendLogoutRequest(LogoutRequest request) throws IOException {
        return new LogoutServiceProxy().logout(request);
    }

    public void sendErrorMessage(Response response) {
        view.displayToast(response.getMessage());
    }

    public void goToWelcomeFragment() {
        ((IActivity)view.getActivity()).goToWelcomeFragment();
    }

    public void update() {
        ((HomeFragment)view).setUser(user);
    }
}
