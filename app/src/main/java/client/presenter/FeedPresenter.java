package client.presenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import model.domain.Status;
import model.domain.User;
import client.model.service.FeedServiceProxy;
import client.net.Model;
import client.view.IView;
import request.FeedRequest;
import request.LogoutRequest;
import response.FeedResponse;
import response.LogoutResponse;
import response.Response;

public class FeedPresenter implements IPresenter, Serializable {
    private IView view;
    private GetFeedAdapter adapter;
    private FeedResponse response;
    private User user;
    private List<Status> pageStatuses;

    /**
     * The interface by which this presenter communicates with its view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }
    public interface GetFeedAdapter {
        void statusesRetrieved(List<Status> statuses, FeedResponse feedResponse);
    }

    public FeedPresenter(IView view) {
        this.view = view;
    }

    public FeedResponse getFeed(FeedRequest request) throws IOException {
        user = request.getUser();
        response = new FeedServiceProxy().getFeed(request);
        return response;
    }

    public void setAdapter(GetFeedAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public void sendErrorMessage(Response response) {
        view.displayToast(response.getMessage());
    }
    @Override
    public void update() {
        pageStatuses = Model.getInstance().getFeed().getPageStatuses();//user.getPageFeed();
        //FeedResponse newResponse = new FeedResponse(pageStatuses, response.hasMorePages());
        if (response != null) {
            adapter.statusesRetrieved(pageStatuses, response);
        }
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}
}
