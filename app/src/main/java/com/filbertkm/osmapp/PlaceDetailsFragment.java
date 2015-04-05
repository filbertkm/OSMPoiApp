package com.filbertkm.osmapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlaceDetailsFragment extends Fragment {

    TextView textView;

    String name = "Place name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        textView = (TextView) view.findViewById(R.id.place_details_name);
        textView.setText(name);

        return view;
    }

    public void setName(String name) {
        this.name = name;
    }

}