package com.filbertkm.osmapp.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmxml.OSMNodeXmlBuilder;

public class AddTagFragment extends Fragment {

    Place place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);

        TextView placeNameView = (TextView) view.findViewById(R.id.add_tag_place_name);
        placeNameView.setText(place.getName());

        view.findViewById(R.id.button_add_tag_submit).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new HttpAsyncTask().execute("x");
                    }
                }
        );

        return view;
    }

    public void setPlace(Place place) { this.place = place; }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            OSMNodeXmlBuilder xmlBuilder = new OSMNodeXmlBuilder();


            return "success!!";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}