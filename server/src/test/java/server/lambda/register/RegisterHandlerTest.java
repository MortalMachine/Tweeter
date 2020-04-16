package server.lambda.register;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

class RegisterHandlerTest {

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
                "https://clipartix.com/wp-content/uploads/2017/10/Mickey-mouse-head-clipart-free-images.gif");
        RegisterRequest request = new RegisterRequest(user, "password");
        RegisterHandler handler = new RegisterHandler();
        RegisterResponse response = handler.handleRequest(request, null);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getToken());
    }
}