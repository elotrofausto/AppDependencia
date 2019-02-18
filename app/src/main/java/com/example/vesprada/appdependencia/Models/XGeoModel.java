package com.example.vesprada.appdependencia.Models;

import java.util.Date;

public class XGeoModel {

    private Date fecha;
    private double lat;
    private double lng;

    public XGeoModel(Date fecha, double lat, double lng) {
        this.fecha = fecha;
        this.lat = lat;
        this.lng = lng;
    }

    public Date getFecha() {
        return fecha;
    }

    public Long getFechaLong(){
        return fecha.getTime();
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
