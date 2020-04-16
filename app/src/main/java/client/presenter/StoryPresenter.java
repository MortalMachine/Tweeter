package client.presenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import client.view.IActivity;
import model.domain.Status;
import model.domain.User;
import client.model.service.StoryServiceProxy;
import client.net.Model;
import response.StoryResponse;
import client.view.IView;
import request.LogoutRequest;
import request.StoryRequest;
import response.LogoutResponse;
import response.Response;

public class StoryPresenter implements IPresenter, Serializable {
    private IView view;
    private GetStoryAdapter adapter;
    private StoryResponse response;
    private User user;
    private List<Status> pageStatuses;

    /**
     * The interface by which this presenter communicates with its view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }
    public interface GetStoryAdapter {
        void statusesRetrieved(List<Status> statuses, StoryResponse storyResponse);
    }

    public StoryPresenter(IView view) {
        this.view = view;
    }

    public StoryResponse getStory(StoryRequest request) throws IOException {
        user = request.getUser();
        response = new StoryServiceProxy().getStory(request);
        return response;
    }

    public void setAdapter(GetStoryAdapter adapter) {
        this.adapter = adapter;
    }

    public void sendErrorMessage(Response response) {
        view.displayToast(response.getMessage());
    }

    public void update() {
        pageStatuses = Model.getInstance().getStory(user).getPageStory();//user.getPageStory();
        //StoryResponse newResponse = new StoryResponse(/*user,*/ pageStatuses, response.hasMorePages());
        if (response != null) {
            adapter.statusesRetrieved(pageStatuses, response);
        }
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}
}
