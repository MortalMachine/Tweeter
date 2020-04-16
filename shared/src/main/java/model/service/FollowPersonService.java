package model.service;

import java.io.IOException;

import request.FollowPersonRequest;
import response.FollowPersonResponse;

public interface FollowPersonService {
    FollowPersonResponse postFollow(FollowPersonRequest request) throws IOException;
}
