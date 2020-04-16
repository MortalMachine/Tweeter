package client.presenter;

import java.io.IOException;
import java.io.Serializable;

import model.domain.User;
import client.model.service.GetUserService;
import client.model.service.RegisterServiceProxy;
import response.RegisterResponse;
import client.view.IActivity;
import client.view.IView;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LogoutResponse;
import response.Response;

public class RegisterPresenter implements IPresenter, Serializable {
    private IView registerView;

    public RegisterPresenter(IView registerView) {
        this.registerView = registerView;
    }

    public RegisterResponse sendRegisterRequest(RegisterRequest request) throws IOException {
        //RegisterService service = new RegisterService();
        //service.registerUser(request);
        //request.getUser().addObserver(this);
        RegisterResponse response = new RegisterServiceProxy().register(request);
        return response;
    }

    public void update() {
        User user = GetUserService.getInstance().getUser().getUser();
        if (user == null) {
            registerView.displayToast("That alias is already taken!");
        }
        else {
            ((IActivity)registerView.getActivity()).goToHomeFragment(user);
        }
    }

    public void sendErrorMessage(Response response) {
        registerView.displayToast(response.getMessage());
    }
    @Override
    public LogoutResponse sendLogoutRequest(LogoutRequest request) { return null; }
    @Override
    public void goToWelcomeFragment() {}
}
