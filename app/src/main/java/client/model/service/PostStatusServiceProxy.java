package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.PostStatusService;
import request.PostStatusRequest;
import response.PostStatusResponse;

public class PostStatusServiceProxy implements PostStatusService {
    private static final String URL_PATH = "/poststatus";

    private ServerFacade serverFacade;

    public PostStatusServiceProxy() {
        serverFacade = new ServerFacade();
    }

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException {
        return serverFacade.postStatus(request, URL_PATH);
    }
}
