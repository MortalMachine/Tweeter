package client.presenter;

import java.io.IOException;

import model.domain.User;
import client.model.service.UnfollowPersonServiceProxy;
import response.UnfollowPersonResponse;
import client.view.IActivity;
import client.view.activity.PersonActivity;
import request.LogoutRequest;
import request.UnfollowPersonRequest;
import response.LogoutResponse;
import response.Response;

public class UnfollowPersonPresenter implements IPresenter {
    private IActivity activity;
    private User person;

    public UnfollowPersonPresenter(IActivity activity) {
        this.activity = activity;
    }

    public UnfollowPersonResponse unfollowPerson(UnfollowPersonRequest request) throws IOException {
        return new UnfollowPersonServiceProxy().postUnfollow(request);
    }

    public void update() {
        ((PersonActivity)activity).changeFabDrawable(true);
        ((PersonActivity)activity).setOnClickListenerToFollow();
    }
    public void sendErrorMessage(Response response) {}
    public LogoutResponse sendLogoutRequest(LogoutRequest request) {
        return null;
    }
    public void goToWelcomeFragment() {}

}
