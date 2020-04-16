package server.dao.post_status;

import request.PostStatusRequest;
import response.PostStatusResponse;

public class PostStatusDummyDAO /*implements IPostStatusDAO*/ {
    //@Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (System.currentTimeMillis() > request.getAuthToken().getExpMs()) {
            request.getAuthToken().setExpMs();
        }
        return new PostStatusResponse(true, null, request.getAuthToken());
    }
}
