package com.filbertkm.osmapi;

import java.io.IOException;
import java.util.List;

import com.filbertkm.osmxml.OSMNode;
import com.filbertkm.osmxml.OSMXmlParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.mapbox.mapboxsdk.geometry.BoundingBox;

import android.util.Log;

public class OSMClient {

    private String baseUri = "http://api.openstreetmap.org/api/0.6";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public String getCapabilities() {
        return doRequest("/capabilities");
    }

    public String getMapData(BoundingBox boundingBox) {
        String apiPath = "/map?bbox="
            + boundingBox.getLonWest() + ","
            + boundingBox.getLatSouth() + ","
            + boundingBox.getLonEast() + ","
            + boundingBox.getLatNorth();

        Log.i("osmapp", apiPath);
        return doRequest(apiPath);
    }

    public List<OSMNode> fetchNodesFromBoundingBox(BoundingBox boundingBox) {
        Log.i("osmapp", "fetching nodes");
        String mapData = this.getMapData(boundingBox);
        OSMXmlParser parser = new OSMXmlParser();
        return parser.parse(mapData);
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
                return null;
            }
        } catch (IOException e) {
            // oops!
            return null;
        }
    }

}
