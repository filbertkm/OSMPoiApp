package com.filbertkm.osmapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.filbertkm.osmapp.R;
import com.filbertkm.osmapp.ui.fragment.LoginFragment;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        LoginFragment fragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

}
