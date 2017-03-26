package com.example.vibodha.pinit.Model;

/**
 * Created by vibodha on 3/26/17.
 */

public class Location {

    private String name;
    private double longitude;
    private double latitude;

    public Location(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocationName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
