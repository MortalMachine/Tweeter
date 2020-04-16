package server.lambda.story;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.domain.Status;
import model.domain.User;
import request.StoryRequest;
import response.StoryResponse;

import static org.junit.jupiter.api.Assertions.*;

class GetStoryHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleRequest() {
        //Get page of dummy statuses
        User user = new User("Mickey",
                "Mouse",
                "the_mouse",
                "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif");
        StoryRequest request = new StoryRequest(user, 10, null);
        GetStoryHandler handler = new GetStoryHandler();
        StoryResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        List<Status> pageStatuses = response.getStatuses();
        assertNotNull(pageStatuses);
        assertTrue(pageStatuses.size() > 0);
        //assertEquals(10, pageStatuses.size());
        //assertTrue(response.hasMorePages);

        //Get another page of dummy data statuses for same user
        StoryRequest request2 = new StoryRequest(user, 10, pageStatuses.get(9));
        StoryResponse response2 = handler.handleRequest(request2, null);

        assertNotNull(response2);
        List<Status> nextPageStatuses = response2.getStatuses();
        assertNotNull(nextPageStatuses);
        assertTrue(nextPageStatuses.size() > 0);
        assertEquals(10, nextPageStatuses.size());
        assertTrue(response2.hasMorePages);
    }
}