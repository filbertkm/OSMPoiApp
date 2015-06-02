package com.filbertkm.osmapp.model;


import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Comparator;
import java.util.Map;

public class Place {

    private Long id;

    private String parentType;

    private String type = "other";

    private String name;

    private LatLng location;

    private Map tags;

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentType() {
        return this.parentType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() { return this.location; }

    public void setTags(Map tags) {
        this.tags = tags;
    }

    public Map getTags() {
        return this.tags;
    }

    public static Comparator PlaceComparator = new Comparator<Place>() {

        public int compare(Place node1, Place node2) {
            String node1Id = node1.getName();
            String node2Id = node2.getName();

            return node1Id.compareTo(node2Id);
        }

    };

}