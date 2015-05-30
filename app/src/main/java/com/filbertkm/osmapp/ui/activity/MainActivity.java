package com.filbertkm.osmapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.fragment.MapFragment;
import com.filbertkm.osmapp.ui.fragment.PlaceListFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<Fragment> fragments;

    private CharSequence mTitle;

    private int toggleFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();

        launchFragment(0);
    }

    private void createFragments() {
        fragments = new ArrayList();

        PlaceListFragment placeListFragment = PlaceListFragment.newInstance();

        fragments.add(MapFragment.newInstance(placeListFragment));
        fragments.add(placeListFragment);
    }

    private void launchFragment(int position) {
        if(fragments == null) {
            createFragments();
        }

        Fragment fragment = fragments.get(position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_nav_drawer, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                this.launchFragment(toggleFragment);

                if(toggleFragment == 1) {
                    toggleFragment = 0;
                    item.setTitle(R.string.action_map);
                } else {
                    toggleFragment = 1;
                    item.setTitle(R.string.action_list);
                }

                return true;
            case R.id.action_settings:
                return true;
            default:
                throw new RuntimeException("Unknown menu item selected");
        }
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }

    }

}