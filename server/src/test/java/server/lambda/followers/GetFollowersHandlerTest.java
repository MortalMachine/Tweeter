package server.lambda.followers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.domain.Status;
import model.domain.User;
import request.FollowersRequest;
import response.FollowersResponse;

import static org.junit.jupiter.api.Assertions.*;

class GetFollowersHandlerTest {

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
        FollowersRequest request = new FollowersRequest(user, 10, null);
        GetFollowersHandler handler = new GetFollowersHandler();
        FollowersResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        List<User> pageFollowers = response.getFollowers();
        assertNotNull(pageFollowers);
        assertTrue(pageFollowers.size() > 0);
        assertEquals(10, pageFollowers.size());
        assertTrue(response.hasMorePages);

        //Get another page of dummy data statuses for same user
        FollowersRequest request2 = new FollowersRequest(user, 10, pageFollowers.get(9));
        FollowersResponse response2 = handler.handleRequest(request2, null);

        assertNotNull(response2);
        List<User> nextPageFollowers = response2.getFollowers();
        assertNotNull(nextPageFollowers);
        assertTrue(nextPageFollowers.size() > 0);
        assertEquals(10, nextPageFollowers.size());
        assertTrue(response2.hasMorePages);
    }
}