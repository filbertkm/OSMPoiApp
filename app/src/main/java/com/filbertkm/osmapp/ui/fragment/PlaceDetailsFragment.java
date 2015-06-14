package com.filbertkm.osmapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.activity.AddTagActivity;
import com.filbertkm.osmapp.ui.activity.EditTagActivity;
import com.filbertkm.osmapp.ui.activity.PlaceDetailsActivity;
import com.filbertkm.osmapp.ui.adapter.PlaceDetailsTagsAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class PlaceDetailsFragment extends Fragment {

    String name = "Place name";

    String placeType = "Place type";

    ArrayList<String> tagKeys;

    ArrayList<String> tagValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        TextView textView = (TextView) view.findViewById(R.id.place_details_name);
        textView.setText(name);

        TextView placeTypeView = (TextView) view.findViewById(R.id.place_details_type);
        placeTypeView.setText(placeType);

        ListView listView = (ListView) view.findViewById(R.id.place_details_tags);
        listView.setAdapter(this.buildAdapter(getActivity()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditTagActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("tagKey", tagKeys.get(position));
                intent.putExtra("tagValue", tagValues.get(position));

                startActivity(intent);
            }

        });

        view.findViewById(R.id.place_details_add_tag_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddTagActivity.class);

                        intent.putExtra("name", name);

                        startActivity(intent);
                    }
                }
        );

        return view;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public void setTagKeys(String tagKeys) {
        this.tagKeys = getArrayListFromString(tagKeys);
    }

    public void setTagValues(String tagValues) {
        this.tagValues = getArrayListFromString(tagValues);
    }

    private PlaceDetailsTagsAdapter buildAdapter(Context context) {
        return new PlaceDetailsTagsAdapter(
            context,
            R.layout.placedetails_item_row,
            tagKeys,
            tagValues
        );
    }

    private ArrayList<String> getArrayListFromString(String string) {
        BufferedReader rdr = new BufferedReader(new StringReader(string));
        ArrayList<String> list = new ArrayList<String>();

        try {
            for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                list.add(line);
            }
            rdr.close();
        } catch (IOException e) {
            // blah
        }

        return list;
    }

}