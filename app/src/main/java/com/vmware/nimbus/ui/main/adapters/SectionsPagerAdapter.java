package com.vmware.nimbus.ui.main.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vmware.nimbus.R;
import com.vmware.nimbus.ui.main.fragments.BlueprintsViewFragment;
import com.vmware.nimbus.ui.main.fragments.DeploymentsViewFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.blueprints_tab, R.string.deployments_tab};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int i) {
        super(fm, i);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a BlueprintsViewFragment (defined as a static inner class below).
        switch(position) {
            case 0:
                return BlueprintsViewFragment.newInstance(position);
            case 1:
                return DeploymentsViewFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}