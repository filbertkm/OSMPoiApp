package com.filbertkm.osmapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.events.DelayedMapListener;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;


public class MapFragment extends Fragment {

    private MapListener mapListener;

    private MapView mapView;

    public static MapFragment newInstance(MapListener mapListener) {
        MapFragment fragment = new MapFragment();
        fragment.setMapListener(new DelayedMapListener(mapListener));

        return fragment;
    }

    public void setMapListener(MapListener mapListener) {
        this.mapListener = mapListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);

        initializeMap();

        return v;
    }

    private void initializeMap() {
        WebSourceTileLayer tileSource = new WebSourceTileLayer(
            "openstreetmap",
            "http://tile.openstreetmap.org/{z}/{x}/{y}.png"
        );

        mapView.setTileSource(tileSource);
        mapView.setZoom(18);
        mapView.setUserLocationEnabled(true);
        mapView.goToUserLocation(true);

        mapView.addListener(this.mapListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}