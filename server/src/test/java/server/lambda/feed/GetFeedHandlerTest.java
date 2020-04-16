package server.lambda.feed;

import java.util.List;

import model.domain.Status;
import model.domain.User;
import request.FeedRequest;
import response.FeedResponse;

import static org.junit.jupiter.api.Assertions.*;

class GetFeedHandlerTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void handleRequestSuccess() {
        //Get page of dummy statuses
        User user = new User("Mickey",
                "Mouse",
                "the_mouse",
                "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif");
        FeedRequest request = new FeedRequest(user, 10, null);
        GetFeedHandler handler = new GetFeedHandler();
        FeedResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        List<Status> pageStatuses = response.getStatuses();
        assertNotNull(pageStatuses);
        //assertTrue(pageStatuses.size() > 0);
        assertEquals(10, pageStatuses.size());
        assertTrue(response.hasMorePages);

        //Get another page of dummy data statuses for same user
        FeedRequest request2 = new FeedRequest(user, 10, pageStatuses.get(9));
        FeedResponse response2 = handler.handleRequest(request2, null);

        assertNotNull(response2);
        List<Status> nextPageStatuses = response2.getStatuses();
        assertNotNull(nextPageStatuses);
        assertTrue(nextPageStatuses.size() > 0);
        assertEquals(10, nextPageStatuses.size());
        assertTrue(response2.hasMorePages);
    }

    @org.junit.jupiter.api.Test
    void handleRequest400Error() {
        //Get page of dummy statuses with null user
        User user = null;
        FeedRequest request = new FeedRequest(user, 10, null);
        GetFeedHandler handler = new GetFeedHandler();
        FeedResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertNotNull(response.getMessage());
        assertFalse(response.getMessage().isEmpty());
    }

}