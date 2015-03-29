package com.filbertkm.osmapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PlaceListAdapter extends ArrayAdapter<Place> {

    public PlaceListAdapter(Context context, int resourceId, List<Place> list) {
        super(context, resourceId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place place = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.placelist_item_row,
                parent,
                false
            );
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.place_name);
        nameTextView.setText(place.getName());

        TextView typeTextView = (TextView) convertView.findViewById(R.id.place_type);
        typeTextView.setText(place.getType());

        return convertView;
    }

}
