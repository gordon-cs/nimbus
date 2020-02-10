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

    /**
     * Constructor for the SectionsPagerAdapter
     * @param context - the context
     * @param fm - the FragmentManager
     * @param i - the index of the FragmentManager
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm, int i) {
        super(fm, i);
        mContext = context;
    }

    /**
     * Instantiates the fragment for the given page.
     * @param position - the position of the fragment
     * @return - either a BlueprintsViewFragment or a DeploymentsViewFragment, depending on the position
     */
    @Override
    public Fragment getItem(int position) {
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

    /**
     * Gets the count of the pages
     * @return - (int) 2, as there are always two pages.
     */
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}