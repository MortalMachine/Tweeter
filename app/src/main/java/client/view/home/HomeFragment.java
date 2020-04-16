package client.view.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import client.view.IView;
import edu.byu.cs.app.R;
import model.domain.User;
import request.LogoutRequest;
import client.presenter.HomePresenter;
import client.view.activity.StatusActivity;
import client.view.asyncTasks.LoadImageTask;
import client.view.asyncTasks.LogoutTask;
import client.view.cache.ImageCache;

public class HomeFragment extends Fragment implements IView, Serializable, LoadImageTask.LoadImageObserver {

    private HomePresenter presenter;
    private User user;
    private ImageView userImageView;
    private Button btnLogout;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private LoadImageTask loadImageTask;

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() { return user; }
    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean isAlreadyLoaded() { return true; }
    @Override
    public void lazyLoad() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* The system calls this when creating the fragment.
        Within your implementation, you should initialize essential components
        of the fragment that you want to retain when the fragment
        is paused or stopped, then resumed.
        */

        presenter = new HomePresenter(this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //GetUserTask getUserTask = new GetUserTask(presenter);
        //getUserTask.execute();
        userImageView = view.findViewById(R.id.userImage);
        // Asynchronously load the user's image
        loadImageTask = new LoadImageTask(this);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutRequest request = new LogoutRequest(user);
                LogoutTask task = new LogoutTask(presenter);
                task.execute(request);
            }
        });

        /*
        The system calls this when it's time for the fragment to draw
        its user interface for the first time.
        To draw a UI for your fragment, you must return a View
        from this method that is the root of your fragment's layout.
        You can return null if the fragment does not provide a UI.
        */
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getChildFragmentManager(), user, user);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new CustomOnPageChangeListener());
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                startActivity(new Intent(getActivity(), StatusActivity.class));
            }
        });

        loadImageTask.execute(user.getImageUrl());

        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = view.findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadImageTask.cancel(true);
    }

    private class CustomOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        public CustomOnPageChangeListener() {
            if (sectionsPagerAdapter.getRegisteredFragment(0) != null) {
                sectionsPagerAdapter.getRegisteredFragment(0).lazyLoad();
            }
        }
        @Override
        public void onPageSelected(int position) {
            IView view = sectionsPagerAdapter.getRegisteredFragment(position);
            view.lazyLoad();
        }
    }

    @Override
    public void imageLoadProgressUpdated(Integer progress) {
        // We're just loading one image. No need to indicate progress.
    }

    @Override
    public void imagesLoaded(Drawable[] drawables) {
        ImageCache.getInstance().cacheImage(user, drawables[0]);

        if(drawables[0] != null) {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

}
