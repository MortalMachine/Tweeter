package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.FeedService;
import request.FeedRequest;
import response.FeedResponse;

public class FeedServiceProxy implements FeedService {
    private static final String URL_PATH = "/feed";

    private final ServerFacade serverFacade;

    public FeedServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public FeedResponse getFeed(FeedRequest request) throws IOException {
        return serverFacade.getFeed(request, URL_PATH);
    }
}
