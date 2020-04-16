package edu.byu.cs.app.presenter;

import androidx.fragment.app.Fragment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import client.net.Model;
import request.RegisterRequest;
import response.RegisterResponse;
import client.presenter.RegisterPresenter;
import client.view.IView;

import static org.junit.jupiter.api.Assertions.*;

class MockRegisterView extends Fragment implements IView {
    public String firstName = "Mickey";
    public String lastName = "Mouse";
    public String alias = "the_mouse";
    public String password = "mickey";
    public String imageUrl = "http://clipart-library.com/images/Lcd5rLkLi.png";
    public void displayToast(String message) {}
    public void lazyLoad() {}
    public boolean isAlreadyLoaded() {return false;}
}

class RegisterPresenterTest {
    private MockRegisterView view;
    private RegisterPresenter presenter;
    private User user;

    @BeforeEach
    void setUp() {
        view = new MockRegisterView();
        user = new User(view.firstName, view.lastName, view.alias, view.imageUrl);
        presenter = new RegisterPresenter(view);
    }

    @AfterEach
    void tearDown() {
        Model.getInstance().clear();
    }

    @Test
    void sendRegisterRequestSuccess() {
        RegisterRequest request = new RegisterRequest(user, view.password);
        RegisterResponse response = presenter.sendRegisterRequest(request);
        assertTrue(response != null);
        assertTrue(response.isSuccess());
    }

    @Test
    void sendRegisterRequestFail() {
        User user2 = new User(
                "Goofy",
                "Dog",
                user.getAlias(),
                "https://www.nicepng.com/png/full/134-1341711_goofy-vector-transparent-clipart-free-library-disney-goofy.png");
        sendRegisterRequestSuccess();
        RegisterRequest request = new RegisterRequest(user2, view.password);
        RegisterResponse response = presenter.sendRegisterRequest(request);
        assertTrue(response != null);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage() != null);
        assertEquals("Alias has already been taken", response.getMessage());
    }

    @Test
    void update() {
        presenter.update();
    }

    @Test
    void sendErrorMessage() {

    }
}