package server.lambda.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import request.LoginRequest;
import response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

class LoginHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleRequest() {
        User user = new User("Mickey",
                "Mouse",
                "the_mouse",
                //"https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif"
                        "/home/jordan/Pictures/Mickey-mouse-head-clipart-free-images.gif");
        LoginRequest request = new LoginRequest(user, "password");
        LoginHandler handler = new LoginHandler();
        LoginResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getAuthToken());
    }
}