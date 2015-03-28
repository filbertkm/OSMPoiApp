package com.filbertkm.osmapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.BoundingBox;


public class MainActivity extends ActionBarActivity
    implements MapFragment.OnBoundingBoxChange,
        PlaceFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
        int currentIndex = mViewPager.getCurrentItem();

        if (currentIndex == 0) {
            //
        } else {
            PlaceFragment.OnFragmentInteractionListener placeFragment =
                    (PlaceFragment.OnFragmentInteractionListener) adapter.instantiateItem(mViewPager, currentIndex);
            placeFragment.onFragmentInteraction(uri);
        }
    }

    public void onBoundingBoxChange(BoundingBox boundingBox) {
        SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
        adapter.updatePlaceFragment(boundingBox);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            createFragments();
        }

        private void createFragments() {
            fragments.add(MapFragment.newInstance());
            fragments.add(PlaceFragment.newInstance());
            fragments.add(PlaceFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        public void updatePlaceFragment(BoundingBox boundingBox) {
            PlaceFragment placeFragment = (PlaceFragment)fragments.get(1);
            placeFragment.updateBoundingBox(boundingBox);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
