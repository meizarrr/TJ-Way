package com.example.meizar.test;

/**
 * Created by meizar on 25/02/18.
 */

public class Bus {
    public String plat;
    public double latitude;
    public double longitude;
    public int passenger_count;
    public String trayek;

    public Bus() {}
    public Bus(String plat, double latitude, double longitude, int passenger_count, String trayek)
    {
        this.plat = plat;
        this.latitude = latitude;
        this.longitude = longitude;
        this. passenger_count = passenger_count;
        this.trayek = trayek;
    }
    public String getPlat() {
        return plat;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public int getPassenger_count() {
        return passenger_count;
    }

    public String getTrayek() {
        return trayek;
    }
}