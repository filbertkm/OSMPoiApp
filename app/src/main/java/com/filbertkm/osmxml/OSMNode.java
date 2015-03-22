package com.filbertkm.osmxml;

import java.util.Map;

public class OSMNode {

    private String id;
    private String lat;
    private String lon;
    private Map<String,String> tags;

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

}
