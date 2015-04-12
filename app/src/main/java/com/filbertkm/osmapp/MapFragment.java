package com.filbertkm.osmapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mapbox.mapboxsdk.events.DelayedMapListener;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;


public class MapFragment extends Fragment {

    private MapListener mapListener;

    private View rootView;

    private MapView mapView;

    private ImageButton myLocationButton;

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
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        }

        mapView = (MapView) rootView.findViewById(R.id.mapview);

        initializeMap();

        myLocationButton = (ImageButton) rootView.findViewById(R.id.myLocationButton);
        setLocationButtonListener();

        return rootView;
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

    private void setLocationButtonListener() {
        if (myLocationButton != null) {
            myLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapView.goToUserLocation(true);
                }
            });
        }
    }

}