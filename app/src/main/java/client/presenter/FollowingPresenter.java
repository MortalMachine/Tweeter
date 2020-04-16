package client.presenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import client.model.service.FollowingServiceProxy;
import model.domain.User;
import model.service.FollowingService;
import client.net.Model;
import client.view.IView;
import request.FollowingRequest;
import request.LogoutRequest;
import response.FollowingResponse;
import response.LogoutResponse;
import response.Response;

public class FollowingPresenter implements IPresenter, Serializable {

    private IView view;
    private GetFolloweesAdapter adapter;
    private FollowingResponse response;
    private User user;
    private List<User> pageFollowings;

    /**
     * The interface by which this presenter communicates with its view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }
    public interface GetFolloweesAdapter {
        void followeesRetrieved(List<User> followees, FollowingResponse followingResponse);
    }

    public FollowingPresenter(IView view) {
        this.view = view;
    }

    public FollowingResponse getFollowing(FollowingRequest request) throws IOException {
        user = request.getFollower();
        FollowingService service = new FollowingServiceProxy();
        response = service.getFollowees(request);
        return response;
    }

    public void setAdapter(GetFolloweesAdapter adapter) {
        this.adapter = adapter;
    }

    public void sendErrorMessage(Response response) {
        view.displayToast(response.getMessage());
    }

    public void update() {
        pageFollowings = Model.getInstance().getPageFollowees(user);//user.getPageFollowings();
        FollowingResponse newResponse = new FollowingResponse(pageFollowings, response.hasMorePages());
        adapter.followeesRetrieved(pageFollowings, response);
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}
}
