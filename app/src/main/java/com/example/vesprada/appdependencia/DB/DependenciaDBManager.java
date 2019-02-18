package com.example.vesprada.appdependencia.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vesprada.appdependencia.Models.XAvisoModel;
import com.example.vesprada.appdependencia.Models.XGeoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependenciaDBManager {
    private DependenciaDBHelper todoListDBHelper;

    public DependenciaDBManager(Context context){
        todoListDBHelper = DependenciaDBHelper.getInstance(context);
    }

    //Operations

    //Table XAvisoModel
    //----------------------------------------------------------------------------------------------

    //Create new row
    public void insertAviso(XAvisoModel aviso){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();

            contentValue.put(DependenciaDBContract.Aviso.DNI, aviso.getDependienteDNI());
            contentValue.put(DependenciaDBContract.Aviso.TIPO, aviso.getTipo());
            contentValue.put(DependenciaDBContract.Aviso.NOMBRE, aviso.getName());
            contentValue.put(DependenciaDBContract.Aviso.DESDE, aviso.getFecDesdeLong());
            contentValue.put(DependenciaDBContract.Aviso.HASTA, aviso.getFecHastaLong());
            contentValue.put(DependenciaDBContract.Aviso.PERIODICIDAD, aviso.getPeriodicidad());
            contentValue.put(DependenciaDBContract.Aviso.FINALIZADO, 0); //Siempre están en finalizado = falss al insertar

            sqLiteDatabase.insert(DependenciaDBContract.Aviso.TABLE_NAME, null, contentValue);
        }
    }

    //Update finished status
    public void setAvisoFinished(Integer id){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();
            contentValue.put(DependenciaDBContract.Aviso.FINALIZADO, 1); // finalizado = true

            sqLiteDatabase.update(DependenciaDBContract.Aviso.TABLE_NAME,  contentValue, DependenciaDBContract.Aviso._ID + " = " + id,null);
        }
    }

    public void setAvisoUnfinished(Integer id){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();
            contentValue.put(DependenciaDBContract.Aviso.FINALIZADO, 0); // finalizado = false

            sqLiteDatabase.update(DependenciaDBContract.Aviso.TABLE_NAME,  contentValue, DependenciaDBContract.Aviso._ID + " = " + id,null);
        }
    }

    public void setAvisoSynced(Integer id){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();
            contentValue.put(DependenciaDBContract.Aviso.FINALIZADO, 2); // Lo marcamos a 2 cuando está sincronizado en Postgres para no volverlo a enviar
            sqLiteDatabase.update(DependenciaDBContract.Aviso.TABLE_NAME,  contentValue, DependenciaDBContract.Aviso._ID + " = " + id,null);
        }
    }

    //Select rows
    public List<XAvisoModel> getAvisoRows(String having){
        List<XAvisoModel> listaAvisos = new ArrayList<>();
        Cursor cursor = null;
        //open database to read
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getReadableDatabase();

        if (sqLiteDatabase != null){
            String[] projection = new String[]{DependenciaDBContract.Aviso._ID,
                    DependenciaDBContract.Aviso.DNI,
                    DependenciaDBContract.Aviso.TIPO,
                    DependenciaDBContract.Aviso.NOMBRE,
                    DependenciaDBContract.Aviso.DESDE,
                    DependenciaDBContract.Aviso.HASTA,
                    DependenciaDBContract.Aviso.PERIODICIDAD,
                    DependenciaDBContract.Aviso.FINALIZADO};

            cursor = sqLiteDatabase.query(DependenciaDBContract.Aviso.TABLE_NAME,
                    projection,
                    null,
                    null,
                    DependenciaDBContract.Aviso._ID,
                    having,
                    null);
        }

        if (cursor != null && cursor.moveToFirst()){
            do {
                Date desde = null, hasta = null;
                Long sDesde = cursor.getLong(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.DESDE));
                Long sHasta = cursor.getLong(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.HASTA));
                desde = new Date(sDesde);
                hasta = new Date(sHasta);

                listaAvisos.add(new XAvisoModel(cursor.getInt(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.DNI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.TIPO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.NOMBRE)),
                        desde,
                        hasta,
                        cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.PERIODICIDAD))));
            }while(cursor.moveToNext());
        }

        return listaAvisos;
    }


    //Table XGeoModel
    //----------------------------------------------------------------------------------------------

    //Create new row
    public void insertGeo(XGeoModel geo){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();

            contentValue.put(DependenciaDBContract.Geo.FECHA,geo.getFechaLong());
            contentValue.put(DependenciaDBContract.Geo.LAT,geo.getLat());
            contentValue.put(DependenciaDBContract.Geo.LONG,geo.getLng());

            sqLiteDatabase.insert(DependenciaDBContract.Geo.TABLE_NAME, null, contentValue);
        }
    }

    //Select rows
    public List<XGeoModel> getGeoRows(String having){
        List<XGeoModel> listaGeo = new ArrayList<>();
        Cursor cursor = null;
        //open database to read
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getReadableDatabase();

        if (sqLiteDatabase != null){
            String[] projection = new String[]{DependenciaDBContract.Geo._ID,
            DependenciaDBContract.Geo.FECHA,
            DependenciaDBContract.Geo.LAT,
            DependenciaDBContract.Geo.LONG};

            cursor = sqLiteDatabase.query(DependenciaDBContract.Geo.TABLE_NAME,
                    projection,
                    null,
                    null,
                    DependenciaDBContract.Geo._ID,
                    having,
                    null);

            if (cursor != null && cursor.moveToFirst()){
                do {
                    Date fecha = null;
                    Long sFecha = cursor.getLong(cursor.getColumnIndexOrThrow(DependenciaDBContract.Geo.FECHA));
                    fecha = new Date(sFecha);

                    listaGeo.add(new XGeoModel(
                            fecha,
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DependenciaDBContract.Geo.LAT)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DependenciaDBContract.Geo.LONG))
                    ));
                }while(cursor.moveToNext());
            }
        }

        return listaGeo;
    }


    public void close(){
        todoListDBHelper.close();
    }



}
