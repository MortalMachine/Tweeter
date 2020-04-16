package model.service;

import java.io.IOException;

import request.FeedRequest;
import response.FeedResponse;

public interface FeedService {
    FeedResponse getFeed(FeedRequest request) throws IOException;
}
