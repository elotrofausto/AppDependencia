package com.example.vesprada.appdependencia.BD;

import com.example.vesprada.appdependencia.AdaptersAndClasses.XAvisoModel;

import java.util.Date;

public class DependenciaDBContract {
    //Common fields to all DB

    //Database Name
    public static final String DB_NAME = "TODOLIST.DB";
    //Database Version
    public static final int DB_VERSION = 1;

    //Private constructor
    private DependenciaDBContract(){

    }

    //Schema

    //TABLE TASKS: Inner class that defines the table Tasks contents
    public static class Aviso{
        //Table name
        public static final String TABLE_NAME = "XAvisoMobile";

        //Column names
        public static final String _ID = "_id";
        public static final String DNI = "dni";
        public static final String TIPO = "tipo";
        public static final String NOMBRE = "nombre";
        public static final String DESDE = "desde";
        public static final String HASTA = "hasta";
        public static final String PERIODICIDAD = "periodicidad";


        //CREATE_TABLE SQL String
        public static final String CREATE_TABLE = "CREATE TABLE " + Aviso.TABLE_NAME
                + " ("
                + Aviso._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Aviso.DNI + " TEXT NOT NULL, "
                + Aviso.TIPO + " TEXT NOT NULL, "
                + Aviso.NOMBRE + " TEXT NOT NULL, "
                + Aviso.DESDE + " DATETIME NOT NULL, "
                + Aviso.HASTA + " DATETIME NOT NULL, "
                + Aviso.PERIODICIDAD + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Aviso.TABLE_NAME;

        //other table definition would come here

    }

    public static class Geo{
        //Table name
        public static final String TABLE_NAME = "XGeoMobile";

        //Column names
        public static final String _ID = "_id";
        public static final String FECHA = "fecha";
        public static final String LAT = "latitud";
        public static final String LONG = "longitud";

        //CREATE_TABLE SQL String
        public static final String CREATE_TABLE = "CREATE TABLE " + Geo.TABLE_NAME
                + " ("
                + Aviso._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Aviso.DNI + " TEXT NOT NULL, "
                + Aviso.TIPO + " TEXT NOT NULL, "
                + Aviso.NOMBRE + " TEXT NOT NULL, "
                + Aviso.DESDE + " DATETIME NOT NULL, "
                + Aviso.HASTA + " DATETIME NOT NULL, "
                + Aviso.PERIODICIDAD + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Aviso.TABLE_NAME;

        //other table definition would come here

    }
}
