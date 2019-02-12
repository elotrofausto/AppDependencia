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
            contentValue.put(DependenciaDBContract.Aviso.DESDE, String.valueOf(aviso.getFecDesde()));
            contentValue.put(DependenciaDBContract.Aviso.HASTA, String.valueOf(aviso.getFecHasta()));
            contentValue.put(DependenciaDBContract.Aviso.PERIODICIDAD, aviso.getPeriodicidad());

            sqLiteDatabase.insert(DependenciaDBContract.Aviso.TABLE_NAME, null, contentValue);
        }
    }

    //Select rows
    public List<XAvisoModel> getAvisoRows(){
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
                    DependenciaDBContract.Aviso.PERIODICIDAD};

            cursor = sqLiteDatabase.query(DependenciaDBContract.Aviso.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);
        }

        if (cursor != null && cursor.moveToFirst()){
            do {
                Date desde = null, hasta = null;
                String sDesde = cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.DESDE));
                String sHasta = cursor.getString(cursor.getColumnIndexOrThrow(DependenciaDBContract.Aviso.HASTA));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    desde = dateFormat.parse(sDesde);
                    hasta = dateFormat.parse(sHasta);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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
    public void insertGeo(XGeoModel aviso){
        //open database to read and write
        SQLiteDatabase sqLiteDatabase = todoListDBHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            ContentValues contentValue = new ContentValues();

            //contentValue.put(DependenciaDBContract.Aviso.DNI, aviso.getDependienteDNI());
            //contentValue.put(DependenciaDBContract.Aviso.TIPO, aviso.getTipo());
            //contentValue.put(DependenciaDBContract.Aviso.NOMBRE, aviso.getName());
            //contentValue.put(DependenciaDBContract.Aviso.DESDE, String.valueOf(aviso.getFecDesde()));
            //contentValue.put(DependenciaDBContract.Aviso.HASTA, String.valueOf(aviso.getFecHasta()));
            //contentValue.put(DependenciaDBContract.Aviso.PERIODICIDAD, aviso.getPeriodicidad());

            sqLiteDatabase.insert(DependenciaDBContract.Geo.TABLE_NAME, null, contentValue);
        }
    }

    //Select rows
    public Cursor getGeoRows(){
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
                    DependenciaDBContract.Aviso.PERIODICIDAD};

            cursor = sqLiteDatabase.query(DependenciaDBContract.Geo.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);
        }

        return cursor;
    }


    public void close(){
        todoListDBHelper.close();
    }



}