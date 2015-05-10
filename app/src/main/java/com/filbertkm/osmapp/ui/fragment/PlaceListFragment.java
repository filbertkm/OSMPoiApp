package com.filbertkm.osmapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.ui.activity.PlaceDetailsActivity;
import com.filbertkm.osmapp.PlaceListUpdater;
import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.adapter.PlaceListAdapter;
import com.mapbox.mapboxsdk.events.MapListener;
import com.mapbox.mapboxsdk.events.RotateEvent;
import com.mapbox.mapboxsdk.events.ScrollEvent;
import com.mapbox.mapboxsdk.events.ZoomEvent;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaceDetailsActivity.class);

                StringBuilder keyStringBuilder = new StringBuilder();
                StringBuilder valueStringBuilder = new StringBuilder();

                Iterator iterator = placeList.get(position).getTags().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry tag = (Map.Entry) iterator.next();
                    keyStringBuilder.append(tag.getKey().toString() + "\n");
                    valueStringBuilder.append(tag.getValue().toString() + "\n");
                }

                intent.putExtra("name", placeList.get(position).getName());
                intent.putExtra("type", placeList.get(position).getType());
                intent.putExtra("tagKeys", keyStringBuilder.toString());
                intent.putExtra("tagValues", valueStringBuilder.toString());

                startActivity(intent);
            }

        });

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

        if (mapView.getZoomLevel() < 17) {
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
        File cacheDir = context.getCacheDir();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir();
        }

        adapter = new PlaceListAdapter(
                context,
                R.layout.placelist_item_row,
                placeList
        );

        placeListUpdater = new PlaceListUpdater(
            adapter,
            placeList,
            cacheDir
        );
    }

}