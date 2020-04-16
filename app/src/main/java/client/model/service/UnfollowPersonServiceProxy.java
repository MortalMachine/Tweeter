package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.UnfollowPersonService;
import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;

public class UnfollowPersonServiceProxy implements UnfollowPersonService {
    private static final String URL_PATH = "/unfollow";
    private ServerFacade serverFacade;

    public UnfollowPersonServiceProxy() {
        serverFacade = new ServerFacade();
    }

    public UnfollowPersonResponse postUnfollow(UnfollowPersonRequest request) throws IOException {
        return serverFacade.unfollowPerson(request, URL_PATH);
    }
}
