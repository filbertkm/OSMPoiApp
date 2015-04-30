package com.filbertkm.osmapi;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.filbertkm.osmxml.OSMNode;
import com.filbertkm.osmxml.OSMXmlParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.util.IOUtils;
import com.jakewharton.disklrucache.DiskLruCache;
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

    private String doOriginalRequest(String apiPath) {
        retries = 0;
        return doRequest(apiPath);
    }

    private String doRequest(String apiPath) {
        GenericUrl url = new GenericUrl(baseUri + apiPath);
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

        try {
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
