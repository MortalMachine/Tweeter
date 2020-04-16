package client.presenter;

import java.io.IOException;

import model.domain.User;
import client.model.service.FollowPersonServiceProxy;
import response.FollowPersonResponse;
import client.view.IActivity;
import client.view.activity.PersonActivity;
import request.FollowPersonRequest;
import request.LogoutRequest;
import response.LogoutResponse;
import response.Response;

public class FollowPersonPresenter implements IPresenter {
    private IActivity activity;
    private User person;

    public FollowPersonPresenter(IActivity activity) {
        this.activity = activity;
    }

    public FollowPersonResponse followPerson(FollowPersonRequest request) throws IOException {
        return new FollowPersonServiceProxy().postFollow(request);
    }

    public void update() {
        ((PersonActivity)activity).changeFabDrawable(false);
        ((PersonActivity)activity).setOnClickListenerToUnfollow();
    }
    public void sendErrorMessage(Response response) {}
    public LogoutResponse sendLogoutRequest(LogoutRequest request) {
        return null;
    }
    public void goToWelcomeFragment() {}
}
