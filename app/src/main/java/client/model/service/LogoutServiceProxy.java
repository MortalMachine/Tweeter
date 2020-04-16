package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.LogoutService;
import request.LogoutRequest;
import response.LogoutResponse;

public class LogoutServiceProxy implements LogoutService {
    private static final String URL_PATH = "/logout";

    private final ServerFacade serverFacade;

    public LogoutServiceProxy() {
        serverFacade = new ServerFacade();
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IOException {
        return serverFacade.logout(request, URL_PATH);
    }
}
