package com.filbertkm.osmapp.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.fragment.PlaceDetailsFragment;


public class PlaceDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            PlaceDetailsFragment fragment = new PlaceDetailsFragment();

            fragment.setName(extras.getString("name"));
            fragment.setPlaceType(extras.getString("type"));
            fragment.setTagKeys(extras.getString("tagKeys"));
            fragment.setTagValues(extras.getString("tagValues"));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
