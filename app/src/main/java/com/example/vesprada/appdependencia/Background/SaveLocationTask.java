package com.example.vesprada.appdependencia.Background;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class SaveLocationTask extends AsyncTask<Location, Void, Location> {
    private final String TAG = SaveLocationTask.class.getSimpleName();
    private Context mContext;

    SaveLocationTask(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    protected Location doInBackground(Location... locations) {
        Location location = locations[0];
        //TODO save location in SQLite

        return location;
    }

    @Override
    protected void onPostExecute(Location result) {
        super.onPostExecute(result);
        Log.i("LOCATIONSERVICE:", "Location saved at local SQLite DataBase" + result.getLatitude() + " " + result.getLongitude());
    }
}