package model.service;

import java.io.IOException;

import request.LogoutRequest;
import response.LogoutResponse;

public interface LogoutService {
    LogoutResponse logout(LogoutRequest request) throws IOException;
}
