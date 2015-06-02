package com.filbertkm.osmapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.filbertkm.osmapp.PlaceListUpdater;
import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;


public class MapFragment extends Fragment {

    private MapListener mapListener;

    private View rootView;

    private MapView mapView;

    private ImageButton locationButton;

    private float zoom = 18;

    private LatLng center;

    public void setMapListener(MapListener mapListener) {
        this.mapListener = mapListener;

        if (mapView != null) {
            mapView.addListener(mapListener);
        }
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
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        }

        mapView = (MapView) rootView.findViewById(R.id.mapview);

        initializeMap();

        locationButton = (ImageButton) rootView.findViewById(R.id.location_button);

        setLocationButtonListener();

        return rootView;
    }

    private void initializeMap() {
        WebSourceTileLayer tileSource = new WebSourceTileLayer(
            "openstreetmap",
            "http://tile.openstreetmap.org/{z}/{x}/{y}.png"
        );

        mapView.setDiskCacheEnabled(true);
        mapView.setTileSource(tileSource);
        mapView.setZoom(zoom);

        if (center == null) {
            mapView.setUserLocationEnabled(true);
            mapView.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);
            mapView.goToUserLocation(true);
        } else {
            mapView.setCenter(center);
        }
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
        if (locationButton != null) {
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapView.setUserLocationEnabled(true);
                    mapView.setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);
                    mapView.goToUserLocation(true);
                }
            });
        }
    }

    public MapView getMapView() {
        return mapView;
    }

}