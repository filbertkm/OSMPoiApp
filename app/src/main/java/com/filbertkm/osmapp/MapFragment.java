package com.filbertkm.osmapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.events.DelayedMapListener;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;


public class MapFragment extends Fragment {

    private MapListener mapListener;

    private MapView mapView;

    private float zoom = 18;

    private LatLng center;

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

        if (savedInstanceState != null) {
            zoom = savedInstanceState.getFloat("zoom");
            center = savedInstanceState.getParcelable("center");
        }
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
        mapView.setZoom(zoom);

        if (center == null) {
            mapView.setUserLocationEnabled(true);
            mapView.goToUserLocation(true);
        } else {
            mapView.setCenter(center);
        }

        mapView.addListener(mapListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        zoom = mapView.getZoomLevel();
        center = mapView.getCenter();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putFloat("zoom", mapView.getZoomLevel());
        savedInstanceState.putParcelable("center", mapView.getCenter());
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