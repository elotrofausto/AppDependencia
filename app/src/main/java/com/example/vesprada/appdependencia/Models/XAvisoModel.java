package com.example.vesprada.appdependencia.Models;


import java.text.SimpleDateFormat;
import java.util.Date;

public class XAvisoModel  implements java.io.Serializable {


    private int id;
    private String dependienteDNI;
    private String tipo;
    private String name;
    private Date fecDesde;
    private Date fecHasta;
    private String periodicidad;

    public XAvisoModel() {

    }

    public XAvisoModel(int id, String dependienteDNI, String tipo, String name, Date fecDesde, Date fecHasta, String periodicidad) {
        this.id = id;
        this.dependienteDNI = dependienteDNI;
        this.tipo = tipo;
        this.name = name;
        this.fecDesde = fecDesde;
        this.fecHasta = fecHasta;
        this.periodicidad = periodicidad;
    }

    public XAvisoModel(int id, String dependienteDNI, String tipo, String name, Date fecDesde, Date fecHasta, String periodicidad, Integer createUid, Date createDate, Integer writeUid, Date writeDate) {
        this.id = id;
        this.dependienteDNI = dependienteDNI;
        this.tipo = tipo;
        this.name = name;
        this.fecDesde = fecDesde;
        this.fecHasta = fecHasta;
        this.periodicidad = periodicidad;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Date getFecDesde()
    {
        return this.fecDesde;
    }

    public Long getFecDesdeLong(){
        return fecDesde.getTime();
    }

    public void setFecDesde(Date fecDesde) {
        this.fecDesde = fecDesde;
    }
    public Date getFecHasta() {
        return this.fecHasta;
    }

    public Long getFecHastaLong(){
        return fecHasta.getTime();
    }

    public void setFecHasta(Date fecHasta) {
        this.fecHasta = fecHasta;
    }
    public String getPeriodicidad() {
        return this.periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }


    public String getDependienteDNI() {
        return dependienteDNI;
    }

    public void setDependienteDNI(String dependienteDNI) {
        this.dependienteDNI = dependienteDNI;
    }

    @Override
    public String toString() {
        return "XAvisoModel{" + "id=" + id + ", Dependiente=" + dependienteDNI + ", tipo=" + tipo + ", name=" + name + ", fecDesde=" + fecDesde + ", fecHasta=" + fecHasta + ", periodicidad=" + periodicidad + '}';
    }

}


