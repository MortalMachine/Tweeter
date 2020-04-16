package model.service;

import java.io.IOException;

import request.LoginRequest;
import response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request) throws IOException;
}
