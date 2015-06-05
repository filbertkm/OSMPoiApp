package com.filbertkm.osmapp.ui;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.ui.fragment.PlaceDetailsFragment;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.Iterator;
import java.util.Map;

public class PlaceTooltip extends InfoWindow {

    private FragmentManager fragmentManager;

    public PlaceTooltip(int layoutResId, MapView mapView, Marker marker, FragmentManager fragmentManager) {
        super(layoutResId, mapView);

        this.fragmentManager = fragmentManager;

        setOnTouchListener(new PlaceTooltipTouchListener(marker));
    }

    private class PlaceTooltipTouchListener implements View.OnTouchListener {

        private Marker marker;

        public PlaceTooltipTouchListener(Marker marker) {
            super();

            this.marker = marker;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i("OSMPoiApp", "click " + marker.getTitle());
                launchPlaceFragment((Place)marker.getRelatedObject());
            }

            return true;
        }

        private void launchPlaceFragment(Place place) {
            PlaceDetailsFragment fragment = new PlaceDetailsFragment();

            fragment.setName(place.getName());
            fragment.setPlaceType(place.getType());

            StringBuilder keyStringBuilder = new StringBuilder();
            StringBuilder valueStringBuilder = new StringBuilder();

            Iterator iterator = place.getTags().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry tag = (Map.Entry) iterator.next();
                keyStringBuilder.append(tag.getKey().toString() + "\n");
                valueStringBuilder.append(tag.getValue().toString() + "\n");
            }

            fragment.setTagKeys(keyStringBuilder.toString());
            fragment.setTagValues(valueStringBuilder.toString());

            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
