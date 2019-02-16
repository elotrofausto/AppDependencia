package com.example.vesprada.appdependencia.Background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

public class SyncService extends JobService {

    private static final String MYPREFS = "MyPrefs";
    private static final String DNI = "dni";
    private static final String NONE = "none";

    @Override
    public boolean onStartJob(JobParameters params) {
        SharedPreferences myPreferences = getSharedPreferences(MYPREFS, this.MODE_PRIVATE);
        String dni = myPreferences.getString(DNI,NONE);

        Log.i("SYNCJOB:","JOB STARTED");
        //Iniciamos una AsyncTask que sincroniza los datos de nuestra SQLite con los del servidor Postgres
        SyncDBTask sync = new SyncDBTask("28888810k","1234", this);
        sync.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
