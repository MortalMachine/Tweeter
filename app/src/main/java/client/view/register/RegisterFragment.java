package client.view.register;

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
import request.RegisterRequest;
import client.presenter.RegisterPresenter;
import client.view.asyncTasks.RegisterTask;

public class RegisterFragment extends Fragment implements IView {
    private RegisterPresenter presenter;
    private RegisterTask registerTask;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etAlias;
    private EditText etPassword;
    private EditText etImageUrl;
    private Button btnRegister;
    private String firstName;
    private String lastName;
    private String alias;
    private String password;
    private String imageUrl;
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
        presenter = new RegisterPresenter(this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
        //registerTask.cancel(true);
    }

    private View wireUpWidgets(View view) {
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etAlias = view.findViewById(R.id.et_alias);
        etPassword = view.findViewById(R.id.et_password);
        etImageUrl = view.findViewById(R.id.et_image_url);
        btnRegister = view.findViewById(R.id.btn_register);
        /* Comment out line below if the fields are no longer pre-filled */
        btnRegister.setEnabled(true);
        return view;
    }

    private void setListeners() {
        setEditTextListener(etFirstName);
        setEditTextListener(etLastName);
        setEditTextListener(etAlias);
        setEditTextListener(etPassword);
        setEditTextListener(etImageUrl);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                alias = etAlias.getText().toString();
                password = etPassword.getText().toString();
                imageUrl = etImageUrl.getText().toString();
                User user = new User(firstName, lastName, alias, imageUrl);
                RegisterRequest request = new RegisterRequest(user, password);
                registerTask = new RegisterTask(presenter);
                registerTask.execute(request);
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
                    btnRegister.setEnabled(true);
                }
                else btnRegister.setEnabled(false);
            }
        });
        return et;
    }

    private boolean allFieldsFilled() {
        if (etFirstName.getText().length() > 0 &&
                etLastName.getText().length() > 0 &&
                etAlias.getText().length() > 0 &&
                etPassword.getText().length() > 0 &&
                etImageUrl.getText().length() > 0
        ) return true;
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
