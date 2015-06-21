package com.filbertkm.osmapp.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlaceDetailsTagsAdapter extends ArrayAdapter<String> {

    private List<String> tagValues;

    public static PlaceDetailsTagsAdapter newFromTags(Context context, int resourceId, Map tags) {
        List<String> tagKeys = new ArrayList<>();
        List<String> tagValues = new ArrayList<>();

        Iterator iterator = tags.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry tagEntry = (Map.Entry) iterator.next();
            tagKeys.add(tagEntry.getKey().toString());
            tagValues.add(tagEntry.getValue().toString());
        }

        return new PlaceDetailsTagsAdapter(context, resourceId, tagKeys, tagValues);
    }

    public PlaceDetailsTagsAdapter(Context context, int resourceId, List<String>tagKeys, List<String> tagValues) {
        super(context, resourceId, tagKeys);

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
