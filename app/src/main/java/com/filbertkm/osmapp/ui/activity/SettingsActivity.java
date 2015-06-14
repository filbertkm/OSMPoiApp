package com.filbertkm.osmapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.filbertkm.osmapp.PlaceListUpdater;
import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.fragment.MapFragment;
import com.filbertkm.osmapp.ui.fragment.PlaceListFragment;
import com.filbertkm.osmapp.ui.fragment.SettingsFragment;
import com.mapbox.mapboxsdk.views.MapView;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment fragment = new SettingsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment, "fragment_settings")
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

}