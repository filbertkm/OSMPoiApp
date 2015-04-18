package com.filbertkm.osmapp;

import android.os.Handler;
import android.widget.ArrayAdapter;

import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PlaceListUpdater {

    private ArrayAdapter adapter;

    private ArrayList<Place> placeList;

    private Handler handler;

    private File cacheDir;

    public PlaceListUpdater(ArrayAdapter adapter, ArrayList<Place> placeList, File cacheDir) {
        this.adapter = adapter;
        this.placeList = placeList;
        this.cacheDir = cacheDir;

        this.handler = new Handler();
    }

    public void updateBoundingBox(BoundingBox boundingBox) {
        final BoundingBox bbox = boundingBox;
        final ArrayList<Place> newPlaceList = new ArrayList<>();

        new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        adapter.clear();
                    }
                });

                OSMClient osmClient = new OSMClient(cacheDir);
                final List<OSMNode> nodes = osmClient.fetchNodesFromBoundingBox(bbox);

                if(nodes == null) {
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
}
