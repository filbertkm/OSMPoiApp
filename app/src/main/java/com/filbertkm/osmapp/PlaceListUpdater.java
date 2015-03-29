package com.filbertkm.osmapp;

import android.os.Handler;
import android.widget.ArrayAdapter;

import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PlaceListUpdater {

    private ArrayAdapter adapter;

    private ArrayList<Place> placeList;

    private Handler handler;

    public PlaceListUpdater(ArrayAdapter adapter, ArrayList<Place> placeList) {
        this.adapter = adapter;
        this.placeList = placeList;
        this.handler = new Handler();
    }

    public void updateBoundingBox(BoundingBox boundingBox) {
        final BoundingBox bbox = boundingBox;

        new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        adapter.clear();
                    }
                });

                OSMClient osmClient = new OSMClient();
                final List<OSMNode> nodes = osmClient.fetchNodesFromBoundingBox(bbox);

                placeList.clear();

                if(nodes == null) {
                    return;
                }

                for (OSMNode node : nodes) {
                    Map tags = node.getTags();

                    if (tags != null) {
                        if (tags.containsKey("name")) {
                            Place place = new Place();
                            Iterator entries = tags.entrySet().iterator();

                            while (entries.hasNext()) {
                                Map.Entry thisEntry = (Map.Entry) entries.next();

                                String tagName = thisEntry.getKey().toString();
                                String tagValue = thisEntry.getValue().toString().replace("_", " ");

                                switch(tagName) {
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

                            placeList.add(place);
                        }
                    }
                }

                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    public ArrayList<Place> getPlaceList() {
        return this.placeList;
    }
}
