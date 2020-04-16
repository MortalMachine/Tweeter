package client.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import client.view.IActivity;
import client.view.IView;
import client.view.cache.ImageCache;
import client.view.home.SectionsPagerAdapter;
import edu.byu.cs.app.R;
import model.domain.User;
import request.FollowPersonRequest;
import request.LogoutRequest;
import request.UnfollowPersonRequest;
import client.presenter.FollowPersonPresenter;
import client.presenter.PersonPresenter;
import client.presenter.UnfollowPersonPresenter;
import client.view.asyncTasks.FollowPersonTask;
import client.view.asyncTasks.LoadImageTask;
import client.view.asyncTasks.LogoutTask;
import client.view.asyncTasks.UnfollowPersonTask;

public class PersonActivity extends AppCompatActivity implements IActivity, LoadImageTask.LoadImageObserver {
    private User loggedInUser;
    private User person;
    private PersonPresenter personPresenter;
    private FollowPersonPresenter followPersonPresenter;
    private UnfollowPersonPresenter unfollowPersonPresenter;
    private Button btnLogout;
    private FloatingActionButton fab;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ImageView userImageView;
    private LoadImageTask loadImageTask;

    @Override
    public void goToHomeFragment(User user) {}
    @Override
    public void goToWelcomeFragment() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadImageTask != null) {
            loadImageTask.cancel(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        personPresenter = new PersonPresenter(this);
        followPersonPresenter = new FollowPersonPresenter(this);
        unfollowPersonPresenter = new UnfollowPersonPresenter(this);
        loggedInUser = (User)getIntent().getSerializableExtra("LoggedInUser");
        person = (User)getIntent().getSerializableExtra("Person");

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutRequest request = new LogoutRequest(loggedInUser);
                LogoutTask task = new LogoutTask(personPresenter);
                task.execute(request);
            }
        });
        btnLogout.setVisibility(View.GONE);

        fab = findViewById(R.id.fab);
        if (personPresenter.personIsUser(person)) {
            fab.setVisibility(View.GONE);
        }
        else if (!personPresenter.userFollowsPerson(person)) {
            changeFabDrawable(true);
            setOnClickListenerToFollow();
        }
        else {
            changeFabDrawable(false);
            setOnClickListenerToUnfollow();
        }

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), loggedInUser, person);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new CustomOnPageChangeListener());
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        userImageView = findViewById(R.id.userImage);
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(person.getImageUrl());

        TextView userName = findViewById(R.id.userName);
        userName.setText(person.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(person.getAlias());

    }

    public void setOnClickListenerToFollow() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowPersonTask followPersonTask = new FollowPersonTask(followPersonPresenter);
                followPersonTask.execute(new FollowPersonRequest(person, null));
            }
        });
    }

    public void setOnClickListenerToUnfollow() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnfollowPersonTask unfollowPersonTask = new UnfollowPersonTask(unfollowPersonPresenter);
                unfollowPersonTask.execute(new UnfollowPersonRequest(person));
            }
        });
    }

    public void changeFabDrawable(boolean needsAddDrawable) {
        if (needsAddDrawable) {
            fab.setImageResource(R.drawable.ic_follow);
        }
        else {
            fab.setImageResource(R.drawable.ic_unfollow);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startTopActivity(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    public void imageLoadProgressUpdated(Integer progress) {
        // We're just loading one image. No need to indicate progress.
    }

    @Override
    public void imagesLoaded(Drawable[] drawables) {
        ImageCache.getInstance().cacheImage(person, drawables[0]);

        if(drawables[0] != null) {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

}
