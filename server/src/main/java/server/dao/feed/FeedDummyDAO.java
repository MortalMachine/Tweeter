package server.dao.feed;

import request.FeedRequest;
import response.FeedResponse;
import server.lambda.StatusGenerator;

public class FeedDummyDAO /*implements IFeedDAO*/ {

    //@Override
    public FeedResponse getFeed(FeedRequest request) throws NullPointerException {
        if (request.getUser() == null) {
            throw new NullPointerException();
        }
        return new FeedResponse(
                StatusGenerator.getInstance().getNStatuses(request.getLimit()),
                true
        );
    }
}
