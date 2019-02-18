package com.example.vesprada.appdependencia.Background;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    private static final int JOB_ID = 1235;
    private static final int PERIOD_MS = 60000;

    public BootReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "LocationService Active", Toast.LENGTH_SHORT).show();
        scheduleJob(context);
    }

    public void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, SaveLocationService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        //builder.setPeriodic(PERIOD_MS);
                builder.setMinimumLatency(PERIOD_MS);
                builder.setOverrideDeadline(PERIOD_MS);
                builder.build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

}
