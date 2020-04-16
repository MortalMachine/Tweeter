package client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.Serializable;

import client.net.Model;
import request.LogoutRequest;
import response.LogoutResponse;
import client.presenter.IPresenter;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> implements Serializable {
    private IPresenter presenter;

    public LogoutTask(IPresenter presenter) {
        this.presenter = presenter;
    }

    protected LogoutResponse doInBackground(LogoutRequest... requests)
    {
        LogoutRequest request = requests[0];
        LogoutResponse response = null;
        try {
            response = presenter.sendLogoutRequest(request);
        }
        catch (IOException e) {
            response = new LogoutResponse(false, e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(LogoutResponse response) {
        if (response.isSuccess()) {
            // Send UI to Welcome View
            presenter.goToWelcomeFragment();
        }
        else {
            presenter.sendErrorMessage(response);
        }
        //response.getUser().removeObserver(presenter);
        Model.getInstance().removeObserver(presenter);
    }

}
