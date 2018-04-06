package com.czsm.Demand_Driver.model;

/**
 * Created by czsm4 on 06/04/18.
 */

public class Data {

    private double lat;
    private double longitude;
    private double distance;

    public Data(double lat, double longitude, double distance) {

        this.lat=lat;
        this.longitude=longitude;
        this.distance=distance;
     }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Data() {

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }






    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }




}

