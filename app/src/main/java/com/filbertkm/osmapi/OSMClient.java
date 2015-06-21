package com.filbertkm.osmapi;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import com.filbertkm.osmxml.OSMPermissionsXmlParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.mapbox.mapboxsdk.geometry.BoundingBox;

public class OSMClient {

    private String baseUri = "http://api.openstreetmap.org/api/0.6";

    private int retries = 0;

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public String getMapData(BoundingBox boundingBox) {
        String bboxName = boundingBox.getLonWest() + ","
                + boundingBox.getLatSouth() + ","
                + boundingBox.getLonEast() + ","
                + boundingBox.getLatNorth();

        String apiPath = "/map?bbox=" + bboxName;
        return doRequest(apiPath);
    }

    public String login(String username, String password) {
        String authString = username + ":" + password;
        String encoding = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

        try {
            GenericUrl url = new GenericUrl(baseUri + "/permissions");
            HttpRequest request = requestFactory.buildGetRequest(url);
            request.getHeaders().setAuthorization("Basic " + encoding);

            HttpResponse response = request.execute();

            if (response.isSuccessStatusCode()) {
                OSMPermissionsXmlParser parser = new OSMPermissionsXmlParser();
                String responseString = response.parseAsString();
                Log.i("OSMPoiApp", responseString);

                if (parser.parse(responseString) == true) {
                    return encoding;
                }
            } else {
                Log.i("osmapp", "response not successful");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String doRequest(String apiPath) {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

        try {
            GenericUrl url = new GenericUrl(baseUri + apiPath);
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse response = request.execute();

            if (response.isSuccessStatusCode()) {
                return response.parseAsString();
            } else {
                Log.i("osmapp", "response not successful");
                return null;
            }
        } catch (IOException e) {
            // oops!
            Log.i("osmapp", "ooops! request failed");
            // try again
            retries++;

            if (retries < 3) {
                return doRequest(apiPath);
            } else {
                return null;
            }
        }
    }
}
