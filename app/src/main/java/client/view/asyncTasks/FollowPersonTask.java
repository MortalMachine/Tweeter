package client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import client.net.Model;
import response.FollowPersonResponse;
import request.FollowPersonRequest;
import client.presenter.FollowPersonPresenter;

public class FollowPersonTask extends AsyncTask<FollowPersonRequest, Void, FollowPersonResponse> {
    private final FollowPersonPresenter presenter;
    private Exception exception;

    public FollowPersonTask(FollowPersonPresenter presenter) {
        this.presenter = presenter;
    }
    @Override
    protected FollowPersonResponse doInBackground(FollowPersonRequest... requests) {
        if (!Model.getInstance().getObservers().contains(presenter)) {
            Model.getInstance().addObserver(presenter);
        }
        FollowPersonResponse response = null;
        try {
            response = presenter.followPerson(requests[0]);
        }
        catch (IOException e) {
            exception = e;
        }
        return response;
    }

    @Override
    protected void onPostExecute(FollowPersonResponse response) {
        if (exception == null && response.isSuccess()) {
            Model.getInstance().notifyObservers();
        }
        else {
            presenter.sendErrorMessage(response);
        }
    }
}
