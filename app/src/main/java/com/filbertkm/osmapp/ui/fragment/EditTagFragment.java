package com.filbertkm.osmapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filbertkm.osmapp.R;

public class EditTagFragment extends Fragment {

    String name = "Place name";

    String placeType = "Place type";

    String tagKey;

    String tagValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_tag, container, false);

        TextView placeNameView = (TextView) view.findViewById(R.id.edit_tag_place_name);
        placeNameView.setText(name);

        TextView placeTypeView = (TextView) view.findViewById(R.id.edit_tag_place_type);
        placeTypeView.setText(placeType);

        TextView tagKeyView = (TextView) view.findViewById(R.id.edit_tag_key);
        tagKeyView.setText(tagKey);

        TextView tagValueView = (TextView) view.findViewById(R.id.edit_tag_value);
        tagValueView.setText(tagValue);

        return view;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

}