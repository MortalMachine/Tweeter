package client.presenter;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.Status;
import model.domain.User;
import client.model.service.PostStatusServiceProxy;
import client.net.Model;
import response.PostStatusResponse;
import client.view.IActivity;
import client.view.activity.StatusActivity;
import request.LogoutRequest;
import request.PostStatusRequest;
import response.LogoutResponse;
import response.Response;

public class StatusPresenter implements IPresenter {
    private IActivity activity;
    private User user;
    private Status status;

    public StatusPresenter(IActivity activity) {
        this.activity = activity;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException {
        return new PostStatusServiceProxy().postStatus(request);
    }

    public void update() {
        Model.getInstance().removeObserver(this);
        ((StatusActivity)activity).startTopActivity((StatusActivity)activity, false);
    }
    public void sendErrorMessage(Response response) {
    }
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    public void goToWelcomeFragment() {}

}
