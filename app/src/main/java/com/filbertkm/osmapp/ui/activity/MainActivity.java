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
import com.mapbox.mapboxsdk.views.MapView;


public class MainActivity extends ActionBarActivity {

    private CharSequence mTitle;

    private PlaceListUpdater placeListUpdater;

    private PlaceListFragment placeListFragment;

    private MapFragment mapFragment;

    private int toggleFragment = 1;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        placeListUpdater = new PlaceListUpdater(this);

        launchFragment(new MapFragment(), "fragment_map");
    }

    private void launchFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem = menu.findItem(R.id.action_toggle);
        MenuItem editItem = menu.findItem(R.id.action_edit);
;
        if(editMode) {
            toggleItem.setVisible(true);
            editItem.setTitle(R.string.action_view);
        } else {
            toggleItem.setVisible(false);
            editItem.setTitle(R.string.action_edit);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (placeListFragment == null) {
            placeListFragment = PlaceListFragment.newInstance();
        }

        placeListFragment.setPlaceListUpdater(placeListUpdater);

        switch (item.getItemId()) {
            case R.id.action_edit:
                if (editMode) {
                    clearMarkers();
                    editMode = false;
                } else {
                    updatePlaces(placeListFragment);
                    editMode = true;
                }

                invalidateOptionsMenu();
                return true;
            case R.id.action_toggle:
                updatePlaces(placeListFragment);

                if(toggleFragment == 1) {
                    toggleFragment = 0;
                    item.setTitle(R.string.action_map);
                    launchFragment(placeListFragment, "fragment_placelist");
                } else {
                    toggleFragment = 1;
                    item.setTitle(R.string.action_list);
                    launchFragment(mapFragment, "fragment_map");
                }

                return true;
            case R.id.action_settings:
                return true;
            default:
                throw new RuntimeException("Unknown menu item selected");
        }
    }

    private void updatePlaces(PlaceListFragment placeListFragment) {
        mapFragment = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("fragment_map");

        if (mapFragment != null) {
            mapFragment.setMapListener(placeListFragment);

            MapView mapView = mapFragment.getMapView();

            if (mapView != null) {
                placeListUpdater.updateBoundingBox(mapView);
            }

            mapView.invalidate();
        }
    }

    private void clearMarkers() {
        mapFragment = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("fragment_map");

        mapFragment.getMapView().clear();
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