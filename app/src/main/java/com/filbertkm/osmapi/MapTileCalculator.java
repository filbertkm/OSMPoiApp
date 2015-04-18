package com.filbertkm.osmapi;

import com.mapbox.mapboxsdk.geometry.BoundingBox;

import java.util.ArrayList;

public class MapTileCalculator {

    public ArrayList<String> calculateTiles(BoundingBox boundingBox) {
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

    public BoundingBox tile2BoundingBox(String tile) {
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

}
