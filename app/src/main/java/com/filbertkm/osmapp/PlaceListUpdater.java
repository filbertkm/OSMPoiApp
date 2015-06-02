package com.filbertkm.osmapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.filbertkm.osmapi.MapTileCalculator;
import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.service.OSMMapDataLoader;
import com.filbertkm.osmapp.ui.adapter.PlaceListAdapter;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.events.RotateEvent;
import com.mapbox.mapboxsdk.events.ScrollEvent;
import com.mapbox.mapboxsdk.events.ZoomEvent;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class PlaceListUpdater implements MapListener {

    private ArrayAdapter adapter;

    private ArrayList<Place> placeList = new ArrayList<>();

    private Handler handler;

    private File cacheDir;

    private MarkerUpdateTask updateTask;

    private MapTileCalculator mapTileCalculator;

    public PlaceListUpdater(Context context) {
        mapTileCalculator = new MapTileCalculator();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }

        this.adapter = new PlaceListAdapter(
                context,
                R.layout.placelist_item_row,
                placeList
        );

        this.handler = new Handler();
    }

    public void updateMapView(MapView mapView) {
        if (mapView.getZoomLevel() < 17) {
            return;
        }

        if (updateTask != null) {
            Log.i("OSMPoiApp", "cancel");
            updateTask.cancel();
        }

        updateTask = new MarkerUpdateTask(mapView);
        updateTask.execute();
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public void onScroll(ScrollEvent event) {
        Log.i("OSMPoiApp", "onScroll");
        updateMapView(event.getSource());
    }

    public void onZoom(ZoomEvent event) {
        Log.i("OSMPoiApp", "onZoom");
        updateMapView(event.getSource());
    }

    public void onRotate(RotateEvent event) {

    }

    private class MarkerUpdateTask extends AsyncTask<Void, Void, String> {

        private MapView mapView;

        private OSMMapDataLoader osmMapDataLoader;

        private boolean cancel = false;

        public MarkerUpdateTask(MapView mapView) {
            this.mapView = mapView;
            this.osmMapDataLoader = new OSMMapDataLoader(cacheDir);
        }

        @Override
        protected String doInBackground (Void... params) {
            if(cancel == false) {
                adapter.clear();

                ArrayList<String> tiles = mapTileCalculator.calculateTiles(mapView.getBoundingBox());

                ArrayList<Place> newPlaceList = new ArrayList<>();

                for (String tile : tiles) {
                    TreeMap<Long, Place> tilePlaces = osmMapDataLoader.getPlacesForTile(tile);

                    for (Map.Entry<Long, Place> entry : tilePlaces.entrySet()) {
                        Place place = entry.getValue();

                        if (place != null) {
                            newPlaceList.add(place);
                        }
                    }
                }

                Collections.sort(newPlaceList, Place.PlaceComparator);

                placeList.clear();
                placeList.addAll(newPlaceList);
                placeList.trimToSize();

                newPlaceList.clear();
            }

            return mapView.getBoundingBox().toString();
        }

        public void cancel() {
            cancel = true;
        }

        @Override
        protected void onPostExecute(String bbox) {
            if (cancel == true) {
                return;
            }

            adapter.notifyDataSetChanged();
            updateMarkers();
        }

        private void updateMarkers() {
            mapView.clear();

            for (Iterator<Place> it = placeList.iterator(); it.hasNext(); ) {
                Place place = it.next();

                Marker marker = new Marker(mapView, place.getName(), place.getType(), place.getLocation());
                Drawable iconImage = ContextCompat.getDrawable(mapView.getContext(), R.drawable.ic_map_marker);
                marker.setIcon(new Icon(iconImage));

                mapView.addMarker(marker);
            }

            mapView.invalidate();
        }

    }
}
