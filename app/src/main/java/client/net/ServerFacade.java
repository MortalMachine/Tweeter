package client.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.domain.Feed;
import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import response.FollowPersonResponse;
import response.PostStatusResponse;
import response.RegisterResponse;
import response.StoryResponse;
import response.UnfollowPersonResponse;
import request.FeedRequest;
import request.FollowPersonRequest;
import request.FollowersRequest;
import request.FollowingRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.PostStatusRequest;
import request.RegisterRequest;
import request.StoryRequest;
import request.UnfollowPersonRequest;
import response.FeedResponse;
import response.FollowersResponse;
import response.FollowingResponse;
import response.GetUserResponse;
import response.LoginResponse;
import response.LogoutResponse;

public class ServerFacade {
    //"Insert your API invoke URL here";
    private static final String SERVER_URL = "INSERT AWS API ARN HERE";

    public ServerFacade() {}

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        RegisterResponse response = clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
        Model.getInstance().setAuthToken(response.getToken());
        Model.getInstance().setUser(response.getUser());
        return response;
    }

    public LoginResponse login(LoginRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Username", request.getUser().getAlias());
        headers.put("Password", request.getPassword());
        LoginResponse response = clientCommunicator.doGet(urlPath, headers, LoginResponse.class);
        Model.getInstance().setAuthToken(response.getAuthToken());
        Model.getInstance().setUser(response.getUser());
        return response;
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Username", request.getUser().getAlias());
        LogoutResponse response = clientCommunicator.doGet(urlPath, headers, LogoutResponse.class);
        return response;
    }

    public GetUserResponse getUser() {
        return Model.getInstance().getUser();
    }

    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
        Model.getInstance().setPageFollowees(request.getFollower(), response.getFollowees());
        Model.getInstance().addFollowees(response.getFollowees());
        return response;
    }

    public FollowersResponse getFollowers(FollowersRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        FollowersResponse response = clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
        Model.getInstance().setPageFollowers(request.getFollowee(), response.getFollowers());
        Model.getInstance().addFollowers(response.getFollowers());
        return response;
    }

    public StoryResponse getStory(StoryRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        StoryResponse response = clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);
        if (!response.isSuccess()) {
            return response;
        }
        Story userStory = Model.getInstance().getStory(request.getUser());
        if (userStory == null) {
            userStory = new Story();
            userStory.setStory(response.getStatuses());
            userStory.setPageStory(response.getStatuses());
        }
        else {
            List<Status> allStatuses = userStory.getStory();
            allStatuses.addAll(response.getStatuses());
            userStory.setStory(allStatuses);
            userStory.setPageStory(response.getStatuses());
        }
        Model.getInstance().setStory(request.getUser(), userStory);
        return response;
    }

    public FeedResponse getFeed(FeedRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        FeedResponse response = clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);
        Feed userFeed = Model.getInstance().getFeed();
        if (userFeed == null) {
            userFeed = new Feed();
            userFeed.setAllStatuses(response.getStatuses());
            userFeed.setPageStatuses(response.getStatuses());
        }
        else {
            List<Status> allStatuses = userFeed.getAllStatuses();
            allStatuses.addAll(response.getStatuses());
            userFeed.setAllStatuses(allStatuses);
            userFeed.setPageStatuses(response.getStatuses());
        }
        return response;
    }

    private Story initializeUserStory(User user) throws Exception {
        Story userStory = StoryGenerator.getInstance().generateStoryForUser(
                10,25, user, StoryGenerator.Sort.NEWEST_TO_OLDEST);
        return userStory;
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.authToken = Model.getInstance().getToken();
        PostStatusResponse response = clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
        return response;
    }

    public FollowPersonResponse followPerson (FollowPersonRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.authToken = Model.getInstance().getToken();
        FollowPersonResponse response = clientCommunicator.doPost(urlPath, request, null, FollowPersonResponse.class);
        Model.getInstance().addFollowee(request.getPerson());
        return response;
    }

    public UnfollowPersonResponse unfollowPerson (UnfollowPersonRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        request.authToken = Model.getInstance().getToken();
        UnfollowPersonResponse response = clientCommunicator.doPost(urlPath, request, null, UnfollowPersonResponse.class);
        Model.getInstance().removeFollowee(request.getPerson());
        return response;
    }

}
