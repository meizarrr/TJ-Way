package com.example.meizar.test;

/**
 * Created by meizar on 07/03/18.
 */

public class Order {
    public String uid;
    public String halteAsalId;
    public String halteTujuanId;
    public String busId;

    public Order() {}
    public Order(String uid, String halteAsalId, String halteTujuanId, String busId){
        this.uid = uid;
        this.halteAsalId = halteAsalId;
        this.halteTujuanId = halteTujuanId;
        this.busId = busId;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
    public void  setHalteAsalId(String halteAsalId){
        this.halteAsalId = halteAsalId;
    }
    public void setHalteTujuanId(String halteTujuanId){
        this.halteTujuanId = halteTujuanId;
    }
    public void setBusId(String busId)
    {
        this.busId = busId;
    }

    public String getUid() {return this.uid;}
    public String getHalteAsalId() {return this.halteAsalId;}
    public String getHalteTujuanId() {return this.halteTujuanId;}
    public String getBusId() {return this.busId;}
}
