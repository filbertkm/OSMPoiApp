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

    private PlaceListUpdater placeListUpdater;

    private PlaceListFragment placeListFragment;

    private MapFragment mapFragment;

    private int toggleFragment = 1;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placeListUpdater = new PlaceListUpdater(this, getSupportFragmentManager());

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
        setEditMenuItem(menu);

        return true;
    }

    private void setEditMenuItem(Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.action_toggle);
        MenuItem editItem = menu.findItem(R.id.action_edit);

        if (editMode) {
            toggleItem.setVisible(true);
            editItem.setTitle(R.string.action_view);
        } else {
            toggleItem.setVisible(false);
            editItem.setTitle(R.string.action_edit);
        }
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
                    updatePlaces();
                    editMode = true;
                }

                invalidateOptionsMenu();
                return true;
            case R.id.action_toggle:
                updatePlaces();

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

    private void updatePlaces() {
        mapFragment = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("fragment_map");

        if (mapFragment != null) {
            mapFragment.setMapListener(placeListUpdater);
            MapView mapView = mapFragment.getMapView();

            if (mapView != null) {
                placeListUpdater.updateMapView(mapView);
            }
        }
    }

    private void clearMarkers() {
        mapFragment = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("fragment_map");

        if (mapFragment != null) {
            MapView mapView = mapFragment.getMapView();

            if (mapView != null) {
                mapFragment.getMapView().clear();
                mapFragment.getMapView().removeListener(placeListUpdater);
            }
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