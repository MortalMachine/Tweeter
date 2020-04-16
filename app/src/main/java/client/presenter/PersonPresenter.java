package client.presenter;

import java.io.IOException;
import java.io.Serializable;

import model.domain.User;
import client.model.service.LogoutServiceProxy;
import client.net.Model;
import client.view.IActivity;
import request.LogoutRequest;
import response.LogoutResponse;
import response.Response;

public class PersonPresenter implements IPresenter, Serializable {
    private IActivity activity;
    private Exception exception;

    public PersonPresenter(IActivity activity) {
        this.activity = activity;
    }

    public boolean personIsUser(User person) {
        return Model.getInstance().isUser(person);
    }

    public boolean userFollowsPerson(User person) {
        return Model.getInstance().hasFollowee(person);
    }

    @Override
    public void update() {}
    @Override
    public void sendErrorMessage(Response response) {}
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) throws IOException {
        return new LogoutServiceProxy().logout(request);
    }
    @Override
    public void goToWelcomeFragment() {

    }

}
