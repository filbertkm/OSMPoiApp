package com.filbertkm.osmapp.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.filbertkm.osmapp.R;

public class AddTagFragment extends Fragment {

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

    public void setName(String name) {
        this.name = name;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return "success";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getBaseContext(), "success!", Toast.LENGTH_LONG).show();
        }
    }

}