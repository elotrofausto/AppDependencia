package com.example.vesprada.appdependencia.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DependenciaDBHelper extends SQLiteOpenHelper {
// instance to SQLiteOpenHelper
    private static DependenciaDBHelper instanceDBHelper;

    public static synchronized DependenciaDBHelper getInstance(Context context){
        //instance must be unique
        if (instanceDBHelper == null){
            instanceDBHelper = new DependenciaDBHelper(context.getApplicationContext());
        }
        return instanceDBHelper;
    }

    private DependenciaDBHelper(Context context) {
        super(context, DependenciaDBContract.DB_NAME,null, DependenciaDBContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DependenciaDBContract.Aviso.CREATE_TABLE);
        db.execSQL(DependenciaDBContract.Geo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //the upgrade policy is simply discard and start over
        db.execSQL(DependenciaDBContract.Aviso.DELETE_TABLE);
        db.execSQL(DependenciaDBContract.Geo.DELETE_TABLE);
        onCreate(db);
    }
}
