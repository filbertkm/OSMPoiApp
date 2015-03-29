package com.filbertkm.osmapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.events.RotateEvent;
import com.mapbox.mapboxsdk.events.ScrollEvent;
import com.mapbox.mapboxsdk.events.ZoomEvent;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PlaceListFragment extends Fragment
    implements MapListener {

    private ListView listView;

    private ArrayList<Place> placeList = new ArrayList<>();

    private ArrayAdapter adapter;

    Handler handler;

    /**
     * @return A new instance of fragment PlaceListFragment.
     */
    public static PlaceListFragment newInstance() {
        PlaceListFragment fragment = new PlaceListFragment();

        return fragment;
    }

    public PlaceListFragment() {
        handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.render();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placelist, container, false);

        listView = (ListView) view.findViewById(R.id.placelist_field);

        adapter = new PlaceListAdapter(
           getActivity(),
           R.layout.placelist_item_row,
           placeList
        );

        listView.setAdapter(adapter);

        return view;
    }

    private void render() {
        new Thread() {
            public void run() {
                OSMClient osmClient = new OSMClient();
          //      final String capabilities = osmClient.getCapabilities();

          /*      handler.post(new Runnable() {
                    public void run() {
                        listView.setText(capabilities);
                    }
                });
            */
            }
        }.start();
    }

    public void updateBoundingBox(BoundingBox boundingBox) {
        final BoundingBox bbox = boundingBox;

        new Thread() {
            public void run() {
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

    public void onScroll(ScrollEvent event) {
        MapView mapView = event.getSource();
        this.updateBoundingBox(mapView.getBoundingBox());
    }

    public void onZoom(ZoomEvent event) {

    }

    public void onRotate(RotateEvent event) {

    }

}