package com.filbertkm.osmapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;

public class EditTagFragment extends Fragment {

    String tagKey;

    String tagValue;

    Place place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_tag, container, false);

        TextView placeNameView = (TextView) view.findViewById(R.id.edit_tag_place_name);
        placeNameView.setText(place.getName());

        TextView tagKeyView = (TextView) view.findViewById(R.id.edit_tag_key);
        tagKeyView.setText(tagKey);

        TextView tagValueView = (TextView) view.findViewById(R.id.edit_tag_value);
        tagValueView.setText(tagValue);

        return view;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public void setPlace(Place place) { this.place = place; }

}