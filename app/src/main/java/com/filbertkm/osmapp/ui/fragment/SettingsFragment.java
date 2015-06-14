package com.filbertkm.osmapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import com.filbertkm.osmapp.R;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
