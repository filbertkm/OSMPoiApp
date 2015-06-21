package com.filbertkm.osmapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class PlaceListFragment extends Fragment
        implements PlaceListUpdater.PlaceListUpdaterListener {

    private ListView listView;

    private ArrayList<Place> placeList = new ArrayList<>();

    PlaceListUpdater placeListUpdater;

    public static PlaceListFragment newInstance() {
        return new PlaceListFragment() ;
    }

    public void setPlaceListUpdater(PlaceListUpdater placeListUpdater) {
        this.placeListUpdater = placeListUpdater;
        this.placeListUpdater.registerPlaceListUpdaterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placelist, container, false);

        listView = (ListView) view.findViewById(R.id.placelist_field);

        listView.setAdapter(placeListUpdater.getAdapter());

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

                intent.putExtra("place", placeList.get(position));

                startActivity(intent);
            }

        });

        return view;
    }

    public void onPlaceListUpdate() {
        placeList = placeListUpdater.getPlaceList();
    }

}