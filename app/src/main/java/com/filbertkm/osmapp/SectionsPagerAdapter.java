package com.filbertkm.osmapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;

import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList();

    private ActionBarActivity activity;

    public SectionsPagerAdapter(FragmentManager fm, ActionBarActivity activity) {
        super(fm);

        this.activity = activity;

        createFragments();
    }

    private void createFragments() {
        PlaceListFragment placeListFragment = PlaceListFragment.newInstance();

        fragments.add(MapFragment.newInstance(placeListFragment));
        fragments.add(placeListFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return this.activity.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return this.activity.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }

}