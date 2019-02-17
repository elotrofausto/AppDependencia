package com.example.vesprada.appdependencia.Background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class SaveLocationService extends JobService {

    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("LOCATIONJOB:","JOB STARTED");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                new SaveLocationTask(SaveLocationService.this)
                        .execute(locationResult.getLastLocation());
            }
        };
        startTrackingLocation();

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
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000 * 60);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

}