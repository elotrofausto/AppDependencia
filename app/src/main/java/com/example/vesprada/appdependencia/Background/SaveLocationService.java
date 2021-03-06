package com.example.vesprada.appdependencia.Background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class SaveLocationService extends JobService {

    //Servicio para guardar periódicamente la localización del dispositivo en SQLite y PostgreSQL

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private final static int PERIOD_MS = 1000 * 60 * 10;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("LOCATIONJOB:","JOB STARTED");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //Es una asynctask la que se encarga de hacer el trabajo
                new SaveLocationTask(SaveLocationService.this)
                        .execute(locationResult.getLastLocation());
            }
        };
        startTrackingLocation();
        //jobFinished(params,true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void startTrackingLocation(){
        try {
            mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback,
                    null /* Looper */);
        }catch (SecurityException e){
            Log.i("SecurityException:", e.getMessage());
        }
    }

    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(PERIOD_MS);
        locationRequest.setFastestInterval(PERIOD_MS / 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

}
