package server.lambda.follow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AuthToken;
import model.domain.User;
import request.FollowPersonRequest;
import response.FollowPersonResponse;

import static org.junit.jupiter.api.Assertions.*;

class FollowPersonHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleRequestSuccess() {
        User personToFollow = new User("Donald",
                "Duck",
                "theduck",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken("themouse");
        FollowPersonRequest request = new FollowPersonRequest(personToFollow, authToken);
        FollowPersonHandler handler = new FollowPersonHandler();

        FollowPersonResponse response = handler.handleRequest(request, null);
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getAuthToken());
        assertNotNull(response.getAuthToken().getAlias());
        assertTrue(response.getAuthToken().getAlias().equals("themouse"));
        assertNull(response.getMessage());
    }

    @Test
    void handleRequest400Error() {
        User personToFollow = null;
        AuthToken authToken = new AuthToken("themouse");
        FollowPersonRequest request = new FollowPersonRequest(personToFollow, authToken);
        FollowPersonHandler handler = new FollowPersonHandler();

        FollowPersonResponse response = handler.handleRequest(request, null);
        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertNotNull(response.getMessage());
        assertFalse(response.getMessage().isEmpty());
    }
}