package client.model.service;

import java.io.IOException;

import model.domain.User;
import client.net.ServerFacade;
import model.service.LoginService;
import request.LoginRequest;
import response.LoginResponse;

public class LoginServiceProxy implements LoginService {
    private static final String URL_PATH = "/login";

    private final ServerFacade serverFacade;

    public LoginServiceProxy() { serverFacade = new ServerFacade(); }

    @Override
    public LoginResponse login(LoginRequest request) throws IOException {
        return serverFacade.login(request, URL_PATH);
    }

}
