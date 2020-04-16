package client.view.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import edu.byu.cs.app.R;
import client.view.login.LoginFragment;
import client.view.register.RegisterFragment;

public class WelcomeFragment extends Fragment {
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
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        /*
        The system calls this when it's time for the fragment to draw
        its user interface for the first time.
        To draw a UI for your fragment, you must return a View
        from this method that is the root of your fragment's layout.
        You can return null if the fragment does not provide a UI.
        */
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
