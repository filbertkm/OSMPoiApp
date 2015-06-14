package com.filbertkm.osmapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.fragment.AddTagFragment;


public class AddTagActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        AddTagFragment fragment = new AddTagFragment();

        Bundle extras = getIntent().getExtras();

        fragment.setName(extras.getString("name"));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

}
