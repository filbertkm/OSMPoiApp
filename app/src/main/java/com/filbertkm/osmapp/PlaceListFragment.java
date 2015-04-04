package com.filbertkm.osmapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.events.RotateEvent;
import com.mapbox.mapboxsdk.events.ScrollEvent;
import com.mapbox.mapboxsdk.events.ZoomEvent;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;


public class PlaceListFragment extends Fragment
    implements MapListener {

    private ListView listView;

    private ArrayList<Place> placeList = new ArrayList<>();

    private ArrayAdapter adapter;

    PlaceListUpdater placeListUpdater;

    /**
     * @return A new instance of fragment PlaceListFragment.
     */
    public static PlaceListFragment newInstance() {
        PlaceListFragment fragment = new PlaceListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placelist, container, false);

        listView = (ListView) view.findViewById(R.id.placelist_field);

        if (placeListUpdater == null) {
            initPlaceListUpdater(getActivity());
        }

        listView.setAdapter(adapter);

        return view;
    }

    public void onScroll(ScrollEvent event) {
        // @fixme add getSource to MapEvent interface
        updatePlaceListFromMapView(event.getSource());
    }

    public void onZoom(ZoomEvent event) {
        updatePlaceListFromMapView(event.getSource());
    }

    public void onRotate(RotateEvent event) {

    }

    private void updatePlaceListFromMapView(MapView mapView) {
        Context context = mapView.getContext();

        if (mapView.getZoomLevel() < 16) {
            return;
        }

        BoundingBox bbox = mapView.getBoundingBox();
        updateBoundingBox(context, bbox);
    }

    public void updateBoundingBox(Context context, BoundingBox boundingBox) {
        if(placeListUpdater == null) {
            initPlaceListUpdater(context);
        }

        placeListUpdater.updateBoundingBox(boundingBox);
        placeList = placeListUpdater.getPlaceList();
    }

    private void initPlaceListUpdater(Context context) {
        adapter = new PlaceListAdapter(
                context,
                R.layout.placelist_item_row,
                placeList
        );

        placeListUpdater = new PlaceListUpdater(adapter, placeList);
    }

}