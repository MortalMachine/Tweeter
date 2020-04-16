package client.presenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import model.domain.User;
import client.model.service.FollowersServiceProxy;
import client.net.Model;
import client.view.IView;
import request.FollowersRequest;
import request.LogoutRequest;
import response.FollowersResponse;
import response.LogoutResponse;
import response.Response;

public class FollowersPresenter implements IPresenter, Serializable {
    private IView view;
    private GetFollowersAdapter adapter;
    private FollowersResponse response;
    private User user;
    private List<User> pageFollowers;

    /**
     * The interface by which this presenter communicates with its view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }
    public interface GetFollowersAdapter {
        void followersRetrieved(List<User> followers, FollowersResponse followingResponse);
    }

    public FollowersPresenter(IView view) {
        this.view = view;
    }

    public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
        user = request.getFollowee();
        response = new FollowersServiceProxy().getFollowers(request);
        return response;
    }

    public void setAdapter(GetFollowersAdapter adapter) {
        this.adapter = adapter;
    }

    public void sendErrorMessage(Response response) {
        view.displayToast(response.getMessage());
    }

    public void update() {
        pageFollowers = Model.getInstance().getPageFollowers(user);//user.getPageFollowers();
        FollowersResponse newResponse = new FollowersResponse(pageFollowers, response.hasMorePages());
        adapter.followersRetrieved(pageFollowers, response);
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}

}
