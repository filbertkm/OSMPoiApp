package com.filbertkm.osmapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;


public class MapFragment extends Fragment {

    private OnBoundingBoxChange mListener;

    MapView mapView;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
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

        v.post(new Runnable() {
            @Override
            public void run() {
                loadOSMLayer();
            }
        });

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
    }

    private void loadOSMLayer() {
        if(mapView.getZoomLevel() < 16) {
            // not allowed
            return;
        }

        BoundingBox boundingBox = mapView.getBoundingBox();

        if (mListener != null) {
            mListener.onBoundingBoxChange(boundingBox);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBoundingBoxChange) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBoundingBoxChange");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBoundingBoxChange {
        public void onBoundingBoxChange(BoundingBox boundingBox);
    }

}
