package com.example.meizar.test;

/**
 * Created by meizar on 09/02/18.
 */

public class Halte {
    public String name;
    public double latitude;
    public double longitude;
    public double jarak;

    public Halte() {}

    public Halte(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getJarak() {return jarak;}

    public void setJarak(double jarak){
        this.jarak = jarak;
    }
}
