package model.service;

import java.io.IOException;

import request.UnfollowPersonRequest;
import response.UnfollowPersonResponse;

public interface UnfollowPersonService {
    UnfollowPersonResponse postUnfollow(UnfollowPersonRequest request) throws IOException;
}
