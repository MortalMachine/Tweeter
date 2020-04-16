package client.view;

import androidx.fragment.app.FragmentActivity;

public interface IView {
    void displayToast(String message);
    FragmentActivity getActivity();
    void lazyLoad();
    boolean isAlreadyLoaded();
}
