package com.filbertkm.osmapp;

import android.util.Log;

import com.filbertkm.osmapi.MapTileCalculator;
import com.filbertkm.osmapi.OSMClient;
import com.filbertkm.osmxml.OSMNode;
import com.filbertkm.osmxml.OSMXmlParser;
import com.jakewharton.disklrucache.DiskLruCache;
import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class OSMMapDataLoader {

    private static final int APP_VERSION = 1;

    private static final int VALUE_COUNT = 1;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 64;

    private File cacheDir;

    private MapTileCalculator mapTileCalculator;

    private OSMClient osmClient;

    public OSMMapDataLoader(File cacheDir) {
        this.cacheDir = new File(cacheDir.getPath() + File.separator + "osmappdata");
        mapTileCalculator = new MapTileCalculator();
        osmClient = new OSMClient();
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

                    if( mapData != null ) {
                        return mapData;
                    }
                } catch (ClassNotFoundException ex) {
                    // omg
                } catch (IOException ex) {
                    // omg
                }
            }

            BoundingBox boundingBox = mapTileCalculator.tile2BoundingBox(tile);
            mapData = osmClient.getMapData(boundingBox);

            if (mapData == null) {
                return null;
            }

            Log.i("osmapp", "fetched map data");

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

    public List<OSMNode> fetchNodesFromBoundingBox(BoundingBox boundingBox) {
        OSMXmlParser parser = new OSMXmlParser();
        ArrayList<String> tiles = mapTileCalculator.calculateTiles(boundingBox);

        TreeMap<Long,OSMNode> bboxNodeList = new TreeMap<>();

        for (String tile : tiles) {
            String mapData = this.getMapData(tile);

            if (mapData == null) {
                Log.i("osmapp", "failed to fetch data");
                return null;
            }

            List<OSMNode> nodeList = parser.parse(mapData);

            Log.i("osmapp", "parsing map data");

            for(OSMNode node : nodeList) {
                bboxNodeList.put(Long.parseLong(node.getId()), node);
            }
        }

        return new ArrayList<>(bboxNodeList.values());
    }

}
