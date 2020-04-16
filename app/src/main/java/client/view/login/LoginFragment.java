package client.view.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import client.view.IView;
import edu.byu.cs.app.R;
import model.domain.User;
import request.LoginRequest;
import client.presenter.LoginPresenter;
import client.view.asyncTasks.LoginTask;

public class LoginFragment extends Fragment implements IView {
    private LoginPresenter presenter;
    private LoginTask loginTask;
    private EditText etAlias;
    private EditText etPassword;
    private Button btnLogin;
    private String alias;
    private String password;
    /*
    Android Lifecycle:
    https://developer.android.com/guide/components/fragments#Creating

    Why use androidx library?
    https://developer.android.com/topic/libraries/support-library/packages.html#v4-fragment
    https://developer.android.com/jetpack/androidx
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* The system calls this when creating the fragment.
        Within your implementation, you should initialize essential components
        of the fragment that you want to retain when the fragment
        is paused or stopped, then resumed.
        */
        presenter = new LoginPresenter(this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        /*
        The system calls this when it's time for the fragment to draw
        its user interface for the first time.
        To draw a UI for your fragment, you must return a View
        from this method that is the root of your fragment's layout.
        You can return null if the fragment does not provide a UI.
        */
        view = wireUpWidgets(view);
        setListeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //loginTask.cancel(true);
    }

    private View wireUpWidgets(View view) {
        etAlias = view.findViewById(R.id.et_alias);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        return view;
    }

    private void setListeners() {
        setEditTextListener(etAlias);
        setEditTextListener(etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alias = etAlias.getText().toString();
                password = etPassword.getText().toString();
                User user = new User(alias);
                LoginRequest request = new LoginRequest(user, password);
                loginTask = new LoginTask(presenter);
                loginTask.execute(request);
            }
        });
    }

    private EditText setEditTextListener(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (allFieldsFilled()) {
                    btnLogin.setEnabled(true);
                }
                else btnLogin.setEnabled(false);
            }
        });
        return et;
    }

    private boolean allFieldsFilled() {
        if (etAlias.getText().length() > 0 &&
                etPassword.getText().length() > 0) return true;
        else return false;
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isAlreadyLoaded() {
        return true;
    }

    @Override
    public void lazyLoad() {}

}
