package com.filbertkm.osmapp.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.filbertkm.osmapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginFragment extends Fragment {

    private String username;
    private String password;

    private EditText usernameField;
    private EditText passwordField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameField = (EditText) view.findViewById(R.id.login_username_field);
        passwordField = (EditText) view.findViewById(R.id.login_password_field);

        view.findViewById(R.id.login_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        username = usernameField.getText().toString();
                        password = passwordField.getText().toString();

                        new HttpAsyncTask().execute(
                                new String[]{username, password}
                        );
                    }
                }
        );

        return view;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i("OSMPoiApp", params[0]);

            try {
                URL reqUrl = new URL("https://api.openstreetmap.org/api/0.6/permissions");

                String authString = params[0] + ":" + params[1];
                String encoding = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);

                try {
                    HttpsURLConnection connection = (HttpsURLConnection) reqUrl.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Basic " + encoding);

                    InputStream content = connection.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(content));

                    String line;

                    while ((line = in.readLine()) != null) {
                        Log.i("OSMPoiApp", line);
                    }
                } catch (IOException ex) {
                    Log.i("OSMPoiApp", "omg");
                    ex.printStackTrace();
                    // omg!
                }
            } catch (MalformedURLException ex) {
                Log.i("OSMPoiApp", "omg");
                ex.printStackTrace();
                // omg!
            }

            return "success!!";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(
                    getActivity().getBaseContext(),
                    "success " + username + "!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}