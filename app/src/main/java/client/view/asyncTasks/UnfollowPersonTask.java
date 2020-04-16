package client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import client.net.Model;
import response.UnfollowPersonResponse;
import request.UnfollowPersonRequest;
import client.presenter.UnfollowPersonPresenter;

public class UnfollowPersonTask extends AsyncTask<UnfollowPersonRequest, Void, UnfollowPersonResponse> {
    private final UnfollowPersonPresenter presenter;
    private Exception exception;

    public UnfollowPersonTask(UnfollowPersonPresenter presenter) {
        this.presenter = presenter;
    }
    @Override
    protected UnfollowPersonResponse doInBackground(UnfollowPersonRequest... requests) {
        if (!Model.getInstance().getObservers().contains(presenter)) {
            Model.getInstance().addObserver(presenter);
        }
        UnfollowPersonResponse response = null;
        try {
            response = presenter.unfollowPerson(requests[0]);
        }
        catch (IOException e) {
            exception = e;
        }
        return response;
    }

    @Override
    protected void onPostExecute(UnfollowPersonResponse response) {
        if (exception == null && response.isSuccess()) {
            Model.getInstance().notifyObservers();
        }
        else {
            presenter.sendErrorMessage(response);
        }
    }
}
