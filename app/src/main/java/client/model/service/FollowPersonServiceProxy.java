package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.FollowPersonService;
import request.FollowPersonRequest;
import response.FollowPersonResponse;

public class FollowPersonServiceProxy implements FollowPersonService {
    private static final String URL_PATH = "/follow";

    private ServerFacade serverFacade;

    public FollowPersonServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public FollowPersonResponse postFollow(FollowPersonRequest request) throws IOException {
        return serverFacade.followPerson(request, URL_PATH);
    }
}
