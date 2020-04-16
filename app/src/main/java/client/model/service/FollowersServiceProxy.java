package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.FollowerService;
import request.FollowersRequest;
import response.FollowersResponse;

public class FollowersServiceProxy implements FollowerService {
    private static final String URL_PATH = "/getfollowers";

    private final ServerFacade serverFacade;

    public FollowersServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
        return serverFacade.getFollowers(request, URL_PATH);
    }
}
