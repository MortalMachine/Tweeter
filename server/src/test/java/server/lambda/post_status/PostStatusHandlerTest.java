package server.lambda.post_status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AuthToken;
import model.domain.Status;
import request.PostStatusRequest;
import response.PostStatusResponse;

import static org.junit.jupiter.api.Assertions.*;

class PostStatusHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleRequest() {
        Status status = new Status();
        status.setMessage("A new status");
        AuthToken authToken = new AuthToken("the_mouse");
        PostStatusRequest request = new PostStatusRequest(status, authToken);

        PostStatusResponse response = new PostStatusHandler().handleRequest(request, null);
        assertNotNull(response);
        assertNotNull(response.getAuthToken());
        assertTrue(response.getSuccess());
        assertTrue(response.getAuthToken().getAlias().equals("the_mouse"));
    }
}