package model.service;

import java.io.IOException;

import request.StoryRequest;
import response.StoryResponse;

public interface StoryService {
    StoryResponse getStory(StoryRequest request) throws IOException;
}
