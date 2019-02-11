package com.example.vesprada.appdependencia.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vesprada.appdependencia.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun clickEvent(v : View){
        var intent = Intent(this, RedButtonActivity::class.java)
        startActivity(intent)
        finish()
    }
}
