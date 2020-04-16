package client.model.service;

import client.net.ServerFacade;
import response.GetUserResponse;

public class GetUserService {
    private static GetUserService instance;
    private final ServerFacade serverFacade;

    private GetUserService() {
        serverFacade = new ServerFacade();
    }

    public static GetUserService getInstance() {
        if (instance == null) {
            instance = new GetUserService();
        }
        return instance;
    }

    public GetUserResponse getUser() {
        return serverFacade.getUser();
    }
}
