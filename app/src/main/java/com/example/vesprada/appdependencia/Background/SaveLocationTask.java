package com.example.vesprada.appdependencia.Background;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vesprada.appdependencia.DB.DependenciaDBManager;
import com.example.vesprada.appdependencia.Models.XGeoModel;

import java.util.Date;

public class SaveLocationTask extends AsyncTask<Location, Void, Location> {
    private final String TAG = SaveLocationTask.class.getSimpleName();
    private Context mContext;
    private DependenciaDBManager db;

    SaveLocationTask(Context applicationContext) {
        mContext = applicationContext;
        db = new DependenciaDBManager(mContext);
    }

    @Override
    protected Location doInBackground(Location... locations) {
        Location location = locations[0];
        //TODO save location in SQLite
        db.insertGeo(new XGeoModel(new Date(),location.getLatitude(),location.getLongitude()));

        return location;
    }

    @Override
    protected void onPostExecute(Location result) {
        super.onPostExecute(result);
        Log.i("LOCATIONSERVICE:", "Location saved at local SQLite DataBase " + result.getLatitude() + " " + result.getLongitude());
    }
}