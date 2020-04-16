package edu.byu.cs.app.presenter;

import androidx.fragment.app.Fragment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import client.net.Model;
import request.FollowersRequest;
import response.FollowersResponse;
import client.view.IView;

import static org.junit.jupiter.api.Assertions.*;

class MockFollowersView extends Fragment implements IView {
    public String alias = "the_mouse";
    public String password = "mickey";
    public void displayToast(String message) {}
    public void lazyLoad() {}
    public boolean isAlreadyLoaded() {return false;}
}

class FollowersPresenterTest {
    private MockFollowersView view;
    private FollowersPresenter presenter;
    private User followee;

    @BeforeEach
    void setUp() {
        view = new MockFollowersView();
        followee = new User(view.alias);
        presenter = new FollowersPresenter(view);
        RegisterPresenterTest regTest = new RegisterPresenterTest();
        regTest.setUp();
        regTest.sendRegisterRequestSuccess();
    }

    @AfterEach
    void tearDown() {
        Model.getInstance().clear();
    }

    @Test
    void getFollowersSuccess() {
        FollowersRequest request = new FollowersRequest(followee, 10, null);
        FollowersResponse response = presenter.getFollowers(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(Model.getInstance().getFollowers(followee));
    }

    @Test
    void setAdapterObserver() {
    }

    @Test
    void sendErrorMessage() {
    }

    @Test
    void update() {
    }
}