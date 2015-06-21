package com.filbertkm.osmapp.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
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

import com.filbertkm.osmapi.OSMClient;
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
            OSMClient client = new OSMClient();
            String loginHash = client.login(params[0], params[1]);

            if (loginHash != null) {
                SharedPreferences preferences = getActivity().getSharedPreferences(
                    "user",
                    Activity.MODE_PRIVATE
                );

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("loginhash", loginHash);

                return "success";
            } else {
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(
                    getActivity().getBaseContext(),
                    result + ": " + username + "!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}