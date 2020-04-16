package client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.Serializable;

import response.GetUserResponse;
import client.presenter.HomePresenter;

public class GetUserTask extends AsyncTask<Void, Void, GetUserResponse> implements Serializable {
    private HomePresenter presenter;
    public GetUserTask(HomePresenter presenter) {
        this.presenter = presenter;
    }

    protected GetUserResponse doInBackground(Void... requests)
    {
        //GetUserResponse response = presenter.getCurrentUser();
        //return response;
        return null;
    }

    @Override
    protected void onPostExecute(GetUserResponse response) {
        if (!response.isSuccess()) {
            presenter.sendErrorMessage(response);
        }
        else {
            //Model.getInstance().notifyObservers();
        }
    }

}
