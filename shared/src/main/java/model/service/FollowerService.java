package model.service;

import java.io.IOException;

import request.FollowersRequest;
import response.FollowersResponse;

public interface FollowerService {
    FollowersResponse getFollowers(FollowersRequest request) throws IOException;
}
