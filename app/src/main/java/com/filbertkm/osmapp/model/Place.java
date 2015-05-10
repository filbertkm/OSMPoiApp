package com.filbertkm.osmapp.model;


import java.util.Comparator;
import java.util.Map;

public class Place {

    private String parentType;

    private String type = "other";

    private String name;

    private Map tags;

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentType() {
        return this.parentType;
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