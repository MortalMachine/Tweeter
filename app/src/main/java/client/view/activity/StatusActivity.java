package client.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import client.net.Model;
import response.PostStatusResponse;
import client.view.IActivity;
import edu.byu.cs.app.R;
import model.domain.Status;
import model.domain.User;
import request.PostStatusRequest;
import client.presenter.StatusPresenter;

public class StatusActivity extends AppCompatActivity implements IActivity {
    private EditText et;
    private Button btnPostStatus;

    private PostStatusTask task;
    private StatusPresenter presenter;

    @Override
    public void goToHomeFragment(User user) {}
    @Override
    public void goToWelcomeFragment() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new StatusPresenter(this);

        et = findViewById(R.id.et_status_field);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnPostStatus.setEnabled(true);
                }
                else btnPostStatus.setEnabled(false);
            }
        });

        btnPostStatus = findViewById(R.id.btn_post_status);
        btnPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status status = new Status();
                status.setMessage(et.getText().toString());
                PostStatusRequest request = new PostStatusRequest(status, null);
                task = new PostStatusTask(presenter);
                task.execute(request);
            }
        });
        //btnLogout.setVisibility(View.GONE);

    }

    private static class PostStatusTask extends AsyncTask<PostStatusRequest, Void, PostStatusResponse> {
        private final StatusPresenter presenter;

        public PostStatusTask(StatusPresenter presenter) {
            this.presenter = presenter;
        }
        @Override
        protected PostStatusResponse doInBackground(PostStatusRequest... requests) {
            if (!Model.getInstance().getObservers().contains(presenter)) {
                Model.getInstance().addObserver(presenter);
            }
            PostStatusResponse response = null;
            try {
                response = presenter.postStatus(requests[0]);
            }
            catch (IOException e) {
                response = new PostStatusResponse(false, e.getMessage(), requests[0].getAuthToken());
            }
            return response;
        }

        @Override
        protected void onPostExecute(PostStatusResponse response) {
            if (response.isSuccess()) {
                Model.getInstance().notifyObservers();
            }
            else {
                presenter.sendErrorMessage(response);
            }
        }
    }

    public void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

}
