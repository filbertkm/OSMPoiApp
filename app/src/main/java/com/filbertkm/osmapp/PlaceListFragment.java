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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PlaceListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView listView;

    private ArrayList<String> placeList = new ArrayList<String>();

    private ArrayAdapter adapter;

    Handler handler;

    /**
     * @return A new instance of fragment PlaceListFragment.
     */
    public static PlaceListFragment newInstance() {
        PlaceListFragment fragment = new PlaceListFragment();

        return fragment;
    }

    public PlaceListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_placelist, container, false);

        listView = (ListView) view.findViewById(R.id.placelist_field);

        adapter = new ArrayAdapter(
           getActivity(),
           android.R.layout.simple_list_item_1,
           placeList
        );

        listView.setAdapter(adapter);

        return view;
    }

    private void render() {
        new Thread() {
            public void run() {
                OSMClient osmClient = new OSMClient();
          //      final String capabilities = osmClient.getCapabilities();

          /*      handler.post(new Runnable() {
                    public void run() {
                        listView.setText(capabilities);
                    }
                });
            */
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
                OSMClient osmClient = new OSMClient();
                final List<OSMNode> nodes = osmClient.fetchNodesFromBoundingBox(bbox);

                if(nodes == null) {
                    return;
                }

                for (OSMNode node : nodes) {
                    Map tags = node.getTags();

                    if (tags != null) {
                        if (tags.containsKey("name")) {
                            Iterator entries = tags.entrySet().iterator();

                            while (entries.hasNext()) {
                                Map.Entry thisEntry = (Map.Entry) entries.next();

                                if (thisEntry.getKey().toString().equals("name")) {
                                    placeList.add(thisEntry.getValue().toString());
                                }
                            }
                        }
                    }
                }

                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
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