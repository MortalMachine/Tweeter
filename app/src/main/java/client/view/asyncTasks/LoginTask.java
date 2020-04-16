package client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.Serializable;

import client.net.Model;
import request.LoginRequest;
import response.LoginResponse;
import client.presenter.LoginPresenter;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> implements Serializable {
    private LoginPresenter presenter;

    public LoginTask(LoginPresenter presenter) { this.presenter = presenter; }

    protected LoginResponse doInBackground(LoginRequest... requests)
    {
        LoginRequest request = requests[0];
        //request.getUser().addObserver(presenter);
        Model.getInstance().addObserver(presenter);
        LoginResponse response = null;
        try {
            response = presenter.sendLoginRequest(request);
        }
        catch (IOException e) {
            response = new LoginResponse(false, e.getMessage(),request.getUser(), null);
        }
        return response;
    }

    @Override
    protected void onPostExecute(LoginResponse response) {
        if (response.isSuccess()) {
            // Notifications happen client-side
            Model.getInstance().notifyObservers();
        }
        else {
            presenter.sendErrorMessage(response);
        }
        Model.getInstance().removeObserver(presenter);
    }

}
