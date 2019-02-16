package com.example.vesprada.appdependencia.Activities

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.vesprada.appdependencia.Background.SyncService
import com.example.vesprada.appdependencia.R

class SplashLoadingActivity : AppCompatActivity() {

    private val JOB_ID = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initJob()
        esperar(3000, this)
    }

    private fun esperar(i: Long, context: Context) {
        var handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run(){
                val intent = Intent(context, RedButtonActivity::class.java)
                startActivity(intent)
                finish()
            }
        },i)
    }

    private fun initJob() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(JobInfo.Builder(JOB_ID,
                ComponentName(this, SyncService::class.java!!))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build())
    }
}
