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

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private static final int APP_VERSION = 1;

    private static final int VALUE_COUNT = 1;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 32;

    private File cacheDir;

    public OSMClient(File cacheDir) {
        this.cacheDir = new File(cacheDir.getPath() + File.separator + "osmappdata");
    }

    public String getMapData(String tile) {
        String mapData = null;

        try {
            DiskLruCache diskLruCache = DiskLruCache.open(
                    this.cacheDir,
                    APP_VERSION,
                    VALUE_COUNT,
                    DISK_CACHE_SIZE
            );

            String cacheKey = tile.replace("/", "_");
            DiskLruCache.Snapshot snapshot = diskLruCache.get(cacheKey);
            if (snapshot != null) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            snapshot.getInputStream(0)
                    );

                    mapData = (String) objectInputStream.readObject();
                    objectInputStream.close();

                    Log.i("osmapp", "obtained tile from cache: " + tile);

                    return mapData;
                } catch (ClassNotFoundException ex) {
                    // omg
                } catch (IOException ex) {
                    // omg
                }
            }

            BoundingBox boundingBox = tile2BoundingBox(tile);

            String bboxName = boundingBox.getLonWest() + ","
                    + boundingBox.getLatSouth() + ","
                    + boundingBox.getLonEast() + ","
                    + boundingBox.getLatNorth();

            String apiPath = "/map?bbox=" + bboxName;
            mapData = doRequest(apiPath);

            try {
                DiskLruCache.Editor editor = diskLruCache.edit(cacheKey);

                ObjectOutputStream out = new ObjectOutputStream(editor.newOutputStream(0));
                out.writeObject(mapData);
                out.close();

                editor.commit();

                Log.i("osmapp", "cached " + tile);
            } catch (IOException ex) {
                // omg!
            }
        } catch (IOException ex) {
            // omg!
        }

        return mapData;
    }

    private ArrayList<String> calculateTiles(BoundingBox boundingBox) {
        int xmin = (int)calculateTileX(boundingBox.getLonWest());
        int xmax = (int)calculateTileX(boundingBox.getLonEast());

        int ymin = (int)calculateTileY(boundingBox.getLatNorth());
        int ymax = (int)calculateTileY(boundingBox.getLatSouth());

        ArrayList<String> tiles = new ArrayList<>();

        for(int x=xmin; x<=xmax; x++) {
            for(int y=ymin; y<=ymax; y++) {
                tiles.add("17/" + x + "/" + y);
            }
        }

        return tiles;
    }

    private double calculateTileX(double lon) {
        return (int) Math.floor((lon + 180) / 360 * (1 << 17));
    }

    private double calculateTileY(double lat) {
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat))
                + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << 17));
    }

    private BoundingBox tile2BoundingBox(String tile) {
        String[] parts = tile.split("/");

        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);

        double north = tile2Lat(y);
        double south = tile2Lat(y+1);
        double west = tile2Lon(x);
        double east = tile2Lon(x + 1);

        return new BoundingBox(north, east, south, west);
    }

    private double tile2Lon(int x) {
        return x / Math.pow(2.0, 17) * 360.0 - 180;
    }

    private double tile2Lat(int y) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, 17);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    public List<OSMNode> fetchNodesFromBoundingBox(BoundingBox boundingBox) {
        OSMXmlParser parser = new OSMXmlParser();
        ArrayList<String> tiles = calculateTiles(boundingBox);

        TreeMap<Long,OSMNode> bboxNodeList = new TreeMap<>();

        for (String tile : tiles) {
            String mapData = this.getMapData(tile);
            List<OSMNode> nodeList = parser.parse(mapData);

            for(OSMNode node : nodeList) {
                bboxNodeList.put(Long.parseLong(node.getId()), node);
            }
        }

        return new ArrayList<>(bboxNodeList.values());
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
