package client.view.home;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.byu.cs.app.R;
import model.domain.User;
import client.view.home.feed.FeedFragment;
import client.view.home.followers.FollowersFragment;
import client.view.home.following.FollowingFragment;
import client.view.home.story.StoryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private int hostActivity;

    private static final int HOST_HOME_FRAGMENT = 0;
    private static final int HOST_PERSON_ACTIVITY = 1;

    private static final int FEED_FRAGMENT_POSITION = 0;
    private static final int STORY_FRAGMENT_POSITION = 1;
    private static final int FOLLOWING_FRAGMENT_POSITION = 2;
    private static final int FOLLOWERS_FRAGMENT_POSITION = 3;

    @StringRes
    private static final int[] FRAGMENT_TAB_TILES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    @StringRes
    private static final int[] ACTIVITY_TAB_TILES = new int[]{R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private Fragment fragment;
    private Context context;
    private User loggedInUser;
    private User user;

    public SectionsPagerAdapter(Fragment fragment, FragmentManager fm, User loggedInUser, User user) {
        /*
        https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter?hl=en#public-constructors

        FragmentPagerAdapter(FragmentManager fm)
        This constructor is deprecated. use FragmentPagerAdapter(FragmentManager, int)
        with BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        */
        super(fm);
        this.fragment = fragment;
        this.loggedInUser = loggedInUser;
        this.user = user;
        this.hostActivity = HOST_HOME_FRAGMENT;
    }

    public SectionsPagerAdapter(Context context, FragmentManager fm, User loggedInUser, User user) {
        super(fm);
        this.context = context;
        this.loggedInUser = loggedInUser;
        this.user = user;
        this.hostActivity = HOST_PERSON_ACTIVITY;
    }

    @Override
    public Fragment getItem(int position) {
        if (hostActivity == HOST_HOME_FRAGMENT) {
            switch (position) {
                case (FEED_FRAGMENT_POSITION):
                    FeedFragment feedFragment = new FeedFragment();
                    feedFragment.setUser(user);
                    return feedFragment;
                case (STORY_FRAGMENT_POSITION):
                    StoryFragment storyFragment = new StoryFragment();
                    storyFragment.setUser(user);
                    storyFragment.setHostType(HOST_HOME_FRAGMENT);
                    return storyFragment;
                case (FOLLOWING_FRAGMENT_POSITION):
                    FollowingFragment followingFragment = new FollowingFragment();
                    followingFragment.setLoggedInUser(loggedInUser);
                    followingFragment.setUser(user);
                    return followingFragment;
                case (FOLLOWERS_FRAGMENT_POSITION):
                    FollowersFragment followersFragment = new FollowersFragment();
                    followersFragment.setUser(user);
                    followersFragment.setLoggedInUser(loggedInUser);
                    return followersFragment;
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }
        else {
            switch (position) {
                case (STORY_FRAGMENT_POSITION - 1):
                    StoryFragment storyFragment = new StoryFragment();
                    storyFragment.setUser(user);
                    storyFragment.setHostType(HOST_PERSON_ACTIVITY);
                    return storyFragment;
                case (FOLLOWING_FRAGMENT_POSITION - 1):
                    FollowingFragment followingFragment = new FollowingFragment();
                    followingFragment.setUser(user);
                    followingFragment.setLoggedInUser(loggedInUser);
                    return followingFragment;
                case (FOLLOWERS_FRAGMENT_POSITION - 1):
                    FollowersFragment followersFragment = new FollowersFragment();
                    followersFragment.setUser(user);
                    followersFragment.setLoggedInUser(loggedInUser);
                    return followersFragment;
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (hostActivity == HOST_HOME_FRAGMENT) {
            return fragment.getResources().getString(FRAGMENT_TAB_TILES[position]);
        }
        else {
            return context.getResources().getString(ACTIVITY_TAB_TILES[position]);
        }
    }

    @Override
    public int getCount() {
        if (hostActivity == HOST_HOME_FRAGMENT) {
            // Show 4 total pages.
            return 4;
        }
        else {
            return 3;
        }
    }
}