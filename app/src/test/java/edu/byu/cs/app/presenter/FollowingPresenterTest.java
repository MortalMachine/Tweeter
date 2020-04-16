package edu.byu.cs.app.presenter;

import androidx.fragment.app.Fragment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import client.net.Model;
import request.FollowingRequest;
import response.FollowingResponse;
import client.presenter.FollowingPresenter;
import client.view.IView;

import static org.junit.jupiter.api.Assertions.*;

class MockFollowingView extends Fragment implements IView {
    public String alias = "the_mouse";
    public String password = "mickey";
    public void displayToast(String message) {}
    public void lazyLoad() {}
    public boolean isAlreadyLoaded() {return false;}
}

class FollowingPresenterTest {
    private MockFollowingView view;
    private FollowingPresenter presenter;
    private User follower;

    @BeforeEach
    void setUp() {
        view = new MockFollowingView();
        follower = new User(view.alias);
        presenter = new FollowingPresenter(view);
        RegisterPresenterTest regTest = new RegisterPresenterTest();
        regTest.setUp();
        regTest.sendRegisterRequestSuccess();
    }

    @AfterEach
    void tearDown() {
        Model.getInstance().clear();
    }

    @Test
    void getFollowingSuccess() {
        FollowingRequest request = new FollowingRequest(follower, 10, null);
        FollowingResponse response = presenter.getFollowing(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(Model.getInstance().getFollowees(follower));
    }
}