package server.lambda.following;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.domain.User;
import request.FollowingRequest;
import response.FollowingResponse;

import static org.junit.jupiter.api.Assertions.*;

class GetFollowingHandlerTest {

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
        FollowingRequest request = new FollowingRequest(user, 10, null);
        GetFollowingHandler handler = new GetFollowingHandler();
        FollowingResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        List<User> pageFollowees = response.getFollowees();
        assertNotNull(pageFollowees);
        //assertTrue(pageFollowees.size() > 0);
        assertEquals(10, pageFollowees.size());
        assertTrue(response.hasMorePages);

        //Get another page of dummy data statuses for same user
        FollowingRequest request2 = new FollowingRequest(user, 10, pageFollowees.get(9));
        FollowingResponse response2 = handler.handleRequest(request2, null);

        assertNotNull(response2);
        List<User> nextPageFollowees = response2.getFollowees();
        assertNotNull(nextPageFollowees);
        assertTrue(nextPageFollowees.size() > 0);
        assertEquals(10, nextPageFollowees.size());
        assertTrue(response2.hasMorePages);
    }
}