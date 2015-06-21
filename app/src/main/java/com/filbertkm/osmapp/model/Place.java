package com.filbertkm.osmapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Comparator;
import java.util.Map;

public class Place implements Parcelable {

    private Long id;

    private String type = "other";
    private String name;
    private Map tags;
    private Map attributes;

    private LatLng location;

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

    public void setAttributes(Map attributes) { this.attributes = attributes; }

    public Map getAttributes() {
        return attributes;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
        dest.writeMap(tags);
        dest.writeMap(attributes);
    }

    public Place() {

    }

    private Place(Parcel parcel) {
        id = parcel.readLong();
        type = parcel.readString();
        name = parcel.readString();
        location = new LatLng(parcel.readDouble(), parcel.readDouble());
        parcel.readMap(tags, Map.class.getClassLoader());
        parcel.readMap(attributes, Map.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Comparator PlaceComparator = new Comparator<Place>() {

        public int compare(Place node1, Place node2) {
            String node1Id = node1.getName();
            String node2Id = node2.getName();

            return node1Id.compareTo(node2Id);
        }

    };

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Place createFromParcel(Parcel parcel) {
            return new Place(parcel);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }

    };

}