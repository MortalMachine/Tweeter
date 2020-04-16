package client.view.home;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.io.Serializable;

import client.view.IView;

public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter implements Serializable {
    // Sparse array to keep track of registered fragments in memory.
    private SparseArray registeredFragments = new SparseArray();

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        /*
        https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter?hl=en#public-constructors

        FragmentPagerAdapter(FragmentManager fm)
        This constructor is deprecated. use FragmentPagerAdapter(FragmentManager, int)
        with BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        */
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // Register the fragment when the item is instantiated
    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @SuppressWarnings("unchecked")
    // Returns the fragment for the position (if instantiated)
    public IView getRegisteredFragment(int position) {
        return (IView) registeredFragments.get(position);
    }
}
