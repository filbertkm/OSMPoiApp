package com.filbertkm.osmapp.ui.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.filbertkm.osmapp.R;

public class AddTagFragment extends Fragment {

    Long nodeId;

    String name = "Place name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);

        TextView placeNameView = (TextView) view.findViewById(R.id.add_tag_place_name);
        placeNameView.setText(name);

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

    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }

    public void setName(String name) {
        this.name = name;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return "success!!";
        }
        
        @Override
        protected void onPostExecute(String result) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String username = preferences.getString("preference_osm_username", null);

            Toast.makeText(
                    getActivity().getBaseContext(),
                    "success " + username + "!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}