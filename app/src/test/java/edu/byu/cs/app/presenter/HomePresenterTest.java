package edu.byu.cs.app.presenter;

import androidx.fragment.app.Fragment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import client.net.Model;
import request.LogoutRequest;
import response.LogoutResponse;
import client.presenter.HomePresenter;
import client.view.IView;
import client.view.home.HomeFragment;

import static org.junit.jupiter.api.Assertions.*;

class MockHomeView extends Fragment implements IView {
    public User user;
    public void displayToast(String message) {}
    public void lazyLoad() {}
    public boolean isAlreadyLoaded() {return false;}
}

class HomePresenterTest {
    private HomeFragment view;
    private HomePresenter presenter;

    @BeforeEach
    void setUp() {
        User user = new User(
                "Mickey",
                "Mouse",
                "the_mouse",
                "http://clipart-library.com/images/Lcd5rLkLi.png");
        view = new HomeFragment();
        view.setUser(user);
        presenter = new HomePresenter(view);
        RegisterPresenterTest regTest = new RegisterPresenterTest();
        regTest.setUp();
        regTest.sendRegisterRequestSuccess();
    }

    @AfterEach
    void tearDown() {
        Model.getInstance().clear();
    }

    @Test
    void sendLogoutRequestSuccess() {
        LogoutRequest request = new LogoutRequest(view.getUser());
        LogoutResponse response = presenter.sendLogoutRequest(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void sendErrorMessage() {
    }

    @Test
    void update() {
    }

}