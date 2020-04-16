package model.service;

import java.io.IOException;

import request.PostStatusRequest;
import response.PostStatusResponse;

public interface PostStatusService {
    PostStatusResponse postStatus(PostStatusRequest request) throws IOException;
}
