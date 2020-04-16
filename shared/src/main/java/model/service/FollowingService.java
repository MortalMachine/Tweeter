package model.service;

import java.io.IOException;

import request.FollowingRequest;
import response.FollowingResponse;

public interface FollowingService {
    FollowingResponse getFollowees(FollowingRequest request) throws IOException;
}
