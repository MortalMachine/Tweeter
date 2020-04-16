package client.model.service;

import java.io.IOException;

import client.net.ServerFacade;
import model.service.RegisterService;
import request.RegisterRequest;
import response.RegisterResponse;

public class RegisterServiceProxy implements RegisterService {
    private static final String URL_PATH = "/register";

    private final ServerFacade serverFacade;

    public RegisterServiceProxy() {
        serverFacade = new ServerFacade();
    }

    public RegisterResponse register(RegisterRequest request) throws IOException {
        return serverFacade.register(request, URL_PATH);
    }
}
