package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.vesprada.appdependencia.R

class SplashLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
}
