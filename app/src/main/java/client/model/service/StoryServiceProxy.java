package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.StoryService;
import request.StoryRequest;
import response.StoryResponse;

public class StoryServiceProxy implements StoryService {
    private static final String URL_PATH = "/story";

    private final ServerFacade serverFacade;

    public StoryServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public StoryResponse getStory(StoryRequest request) throws IOException {
        return serverFacade.getStory(request, URL_PATH);
    }
}
