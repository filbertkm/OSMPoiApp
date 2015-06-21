package com.filbertkm.osmapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.model.Place;
import com.filbertkm.osmapp.ui.fragment.EditTagFragment;
import com.filbertkm.osmapp.ui.fragment.PlaceDetailsFragment;


public class EditTagActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        EditTagFragment fragment = new EditTagFragment();

        Bundle extras = getIntent().getExtras();

        Place place = extras.getParcelable("place");

        fragment.setTagKey(extras.getString("tagKey"));
        fragment.setTagValue(extras.getString("tagValue"));
        fragment.setPlace(place);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

}
