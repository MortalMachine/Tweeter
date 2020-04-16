package server.dao.post_status;

import request.PostStatusRequest;
import response.PostStatusResponse;

public interface IPostStatusDAO {
    void postStatus(PostStatusRequest request);
}
