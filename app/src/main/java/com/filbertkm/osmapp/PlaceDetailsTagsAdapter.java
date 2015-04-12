package com.filbertkm.osmapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceDetailsTagsAdapter extends ArrayAdapter<String> {

    private ArrayList tagValues;

    public PlaceDetailsTagsAdapter(Context context, int resourceId, List<String> list, ArrayList<String> tagValues) {
        super(context, resourceId, list);
        this.tagValues = tagValues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String key = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.placedetails_item_row,
                parent,
                false
            );
        }

        TextView keyView = (TextView) convertView.findViewById(R.id.place_details_row_key);
        keyView.setText(key);

        String value = tagValues.get(position).toString();

        if (value != null) {
            TextView valueView = (TextView) convertView.findViewById(R.id.place_details_row_value);
            valueView.setText(tagValues.get(position).toString());
        }

        return convertView;
    }

}
