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
import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.ui.activity.AddTagActivity;
import com.filbertkm.osmapp.ui.activity.EditTagActivity;
import com.filbertkm.osmapp.ui.adapter.PlaceDetailsTagsAdapter;

public class PlaceDetailsFragment extends Fragment {

    Place place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        TextView textView = (TextView) view.findViewById(R.id.place_details_name);
        textView.setText(place.getName());

        TextView placeTypeView = (TextView) view.findViewById(R.id.place_details_type);
        placeTypeView.setText(place.getType());

        ListView listView = (ListView) view.findViewById(R.id.place_details_tags);
        listView.setAdapter(this.buildAdapter(getActivity()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditTagActivity.class);

                intent.putExtra("place", place);

                startActivity(intent);
            }

        });

        view.findViewById(R.id.place_details_add_tag_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddTagActivity.class);

                        intent.putExtra("place", place);

                        startActivity(intent);
                    }
                }
        );

        return view;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    private PlaceDetailsTagsAdapter buildAdapter(Context context) {
        return PlaceDetailsTagsAdapter.newFromTags(
            context,
            R.layout.placedetails_item_row,
            place.getTags()
        );
    }

}