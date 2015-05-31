package com.filbertkm.osmapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.service.OSMMapDataLoader;
import com.filbertkm.osmapp.ui.adapter.PlaceListAdapter;
import com.filbertkm.osmapp.ui.fragment.MapFragment;
import com.filbertkm.osmxml.OSMNode;
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


public class PlaceListUpdater implements MapFragment.MarkerAdapter {

    private ArrayAdapter adapter;

    private ArrayList<Place> placeList = new ArrayList<>();

    private Handler handler;

    private File cacheDir;

    public PlaceListUpdater(Context context) {
        Log.i("OSMPoiApp", "place list updater");

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

    public void updateBoundingBox(MapView mv) {
        if (mv.getZoomLevel() < 17) {
            return;
        }

        final MapView mapView = mv;
        final BoundingBox bbox = mapView.getBoundingBox();
        final ArrayList<Place> newPlaceList = new ArrayList<>();

        new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        adapter.clear();
                    }
                });

                OSMMapDataLoader osmMapDataLoader = new OSMMapDataLoader(cacheDir);
                final List<OSMNode> nodes = osmMapDataLoader.fetchNodesFromBoundingBox(bbox);

                if (nodes == null) {
                    return;
                }

                for (OSMNode node : nodes) {
                    Place newPlace = getPlaceForNode(node);

                    if (newPlace != null) {
                        newPlaceList.add(newPlace);
                    }
                }

                Collections.sort(newPlaceList, Place.PlaceComparator);

                placeList.clear();
                placeList.addAll(newPlaceList);
                placeList.trimToSize();

                newPlaceList.clear();

                handler.post(new Runnable() {
                    public void run() {
                    onMarkerUpdate(mapView, placeList);
                    adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();

    }

    private Place getPlaceForNode(OSMNode node) {
        Map tags = node.getTags();

        if (tags != null) {
            if (tags.containsKey("name")) {
                Place place = new Place();

                place.setLocation(
                    new LatLng(
                        Double.parseDouble(node.getLat()),
                        Double.parseDouble(node.getLon())
                    )
                );

                Iterator entries = tags.entrySet().iterator();

                while (entries.hasNext()) {
                    Map.Entry thisEntry = (Map.Entry) entries.next();

                    String tagName = thisEntry.getKey().toString();
                    String tagValue = thisEntry.getValue().toString().replace("_", " ");

                    switch (tagName) {
                        case "name":
                            place.setName(tagValue);
                            break;
                        case "amenity":
                            place.setType(tagValue);
                            break;
                        case "shop":
                            place.setType(tagValue);
                            break;
                        default:
                            break;
                    }
                }

                place.setTags(tags);

                return place;
            }
        }

        return null;
    }

    public ArrayList<Place> getPlaceList() {
        return placeList;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public void onMarkerUpdate(MapView mapView, ArrayList<Place> placeList) {
        Log.i("OSMPoiApp", "marker update");
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
