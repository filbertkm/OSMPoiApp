package com.filbertkm.osmapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PlaceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView capabilitiesField;

    Handler handler;

    /**
     * @return A new instance of fragment PlaceFragment.
     */
    public static PlaceFragment newInstance() {
        PlaceFragment fragment = new PlaceFragment();

        return fragment;
    }

    public PlaceFragment() {
        handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.render();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        capabilitiesField = (TextView) view.findViewById(R.id.capabilities_field);

        return view;
    }

    private void render() {
        new Thread() {
            public void run() {
                OSMClient osmClient = new OSMClient();
                final String capabilities = osmClient.getCapabilities();

                handler.post(new Runnable() {
                    public void run() {
                        capabilitiesField.setText(capabilities);
                    }
                });
            }
        }.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void updateBoundingBox(BoundingBox boundingBox) {
        final BoundingBox bbox = boundingBox;

        new Thread() {
            public void run() {
                Log.i("osmapp", "update bounding box");
                OSMClient osmClient = new OSMClient();
                final List<OSMNode> nodes = osmClient.fetchNodesFromBoundingBox(bbox);

                if(nodes == null) {
                    return;
                }

                String output = "";

                for (OSMNode node : nodes) {
                    Map tags = node.getTags();

                    if (tags != null) {
                        if (tags.containsKey("name")) {
                            Iterator entries = tags.entrySet().iterator();

                            while (entries.hasNext()) {
                                Map.Entry thisEntry = (Map.Entry) entries.next();

                                output +=
                                        " - " + thisEntry.getKey() +
                                        " - " + thisEntry.getValue() + "\n";
                            }
                        }
                    }
                }

                final String textOutput = output;

                handler.post(new Runnable() {
                    public void run() {
                        capabilitiesField.setText(textOutput);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
