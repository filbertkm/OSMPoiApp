package com.filbertkm.osmxml;

import android.util.ArrayMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSMNode {

    private String id;
    private String lat;
    private String lon;
    private Map<String,String> tags;
    private Map<String,String> attributes = new HashMap<>();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLon() {
        return this.lon;
    }

    public void setTags(Map<String,String> tags) {
        this.tags = tags;
    }

    public Map<String,String> getTags() {
        return this.tags;
    }

    public void setAttibute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String,String> getAttributes() {
        return attributes;
    }

}
