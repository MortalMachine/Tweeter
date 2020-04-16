package model.service;

import java.io.IOException;

import request.RegisterRequest;
import response.RegisterResponse;

public interface RegisterService {
    RegisterResponse register(RegisterRequest request) throws IOException;
}
