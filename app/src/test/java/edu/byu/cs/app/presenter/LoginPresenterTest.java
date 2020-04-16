package edu.byu.cs.app.presenter;

import androidx.fragment.app.Fragment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import client.net.Model;
import request.LoginRequest;
import response.LoginResponse;
import client.presenter.LoginPresenter;
import client.view.IView;

import static org.junit.jupiter.api.Assertions.*;

class MockLoginView extends Fragment implements IView {
    public String alias = "the_mouse";
    public String password = "mickey";
    public void displayToast(String message) {}
    public void lazyLoad() {}
    public boolean isAlreadyLoaded() {return false;}
}

class LoginPresenterTest {
    private MockLoginView view;
    private LoginPresenter presenter;
    private User user;

    @BeforeEach
    void setUp() {
        view = new MockLoginView();
        user = new User(view.alias);
        presenter = new LoginPresenter(view);
        RegisterPresenterTest regTest = new RegisterPresenterTest();
        regTest.setUp();
        regTest.sendRegisterRequestSuccess();
    }

    @AfterEach
    void tearDown() {
        Model.getInstance().clear();
    }

    @Test
    void sendLoginRequestSuccess() {
        LoginRequest request = new LoginRequest(user, view.password);
        LoginResponse response = presenter.sendLoginRequest(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void sendLoginRequestFail_WrongPassword() {
        User user2 = new User(user.getAlias());
        String password2 = "wrong-password";
        sendLoginRequestSuccess();
        LoginRequest request = new LoginRequest(user2, password2);
        LoginResponse response = presenter.sendLoginRequest(request);
        assertTrue(response != null);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage() != null);
        assertEquals("Alias or Password Not Found or Incorrect", response.getMessage());
    }

    @Test
    void sendLoginRequestFail_WrongAlias() {
        User user2 = new User("wrong_alias");
        sendLoginRequestSuccess();
        LoginRequest request = new LoginRequest(user2, view.password);
        LoginResponse response = presenter.sendLoginRequest(request);
        assertTrue(response != null);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage() != null);
        assertEquals("Alias or Password Not Found or Incorrect", response.getMessage());
    }

    @Test
    void update() {
    }

    @Test
    void sendErrorMessage() {
    }
}