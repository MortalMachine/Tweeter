package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.FollowingService;
import request.FollowingRequest;
import response.FollowingResponse;

public class FollowingServiceProxy implements FollowingService {
    private static final String URL_PATH = "/getfollowing";

    private final ServerFacade serverFacade;

    public FollowingServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) throws IOException {
        return serverFacade.getFollowees(request, URL_PATH);
    }
}
