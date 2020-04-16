package client.view.asyncTasks;

import android.os.AsyncTask;
import android.view.Display;

import java.io.IOException;
import java.io.Serializable;

import client.net.Model;
import response.RegisterResponse;
import request.RegisterRequest;
import client.presenter.RegisterPresenter;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> implements Serializable {
    private RegisterPresenter presenter;

    public RegisterTask(RegisterPresenter presenter) {
        this.presenter = presenter;
    }

    protected RegisterResponse doInBackground(RegisterRequest... requests)
    {
        RegisterRequest request = requests[0];
        //request.getUser().addObserver(presenter);
        Model.getInstance().addObserver(presenter);

        RegisterResponse response = null;
        try {
            response = presenter.sendRegisterRequest(request);
        }
        catch (IOException e) {
            response = new RegisterResponse(false, e.getMessage(), request.getUser(), null);
        }
        return response;
    }

    @Override
    protected void onPostExecute(RegisterResponse response) {
        if (response.isSuccess()) {
            // Notifications happen client-side
            //response.getUser().notifyObservers();
            Model.getInstance().notifyObservers();
        }
        else {
            presenter.sendErrorMessage(response);
        }
        //response.getUser().removeObserver(presenter);
        Model.getInstance().removeObserver(presenter);
    }
}
