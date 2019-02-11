package com.example.vesprada.appdependencia.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vesprada.appdependencia.AdaptersAndClasses.XAvisoModel;

public class DependenciaDBManager {
    private DependenciaDBHelper todoListDBHelper;

    public DependenciaDBManager(Context context){
        todoListDBHelper = DependenciaDBHelper.getInstance(context);
    }

    //Operations

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
    public Cursor getAvisoRows(){
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

        return cursor;
    }

    public void close(){
        todoListDBHelper.close();
    }



}
