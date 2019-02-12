package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.vesprada.appdependencia.R

class LoginActivity : AppCompatActivity() {

    val MYPREFS = "MyPrefs"

    lateinit var dni: EditText
    lateinit var passwd : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUI()

        conprobarInicio()
    }

    private fun conprobarInicio() {

        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

        if(!myPreferences.getString("dni", "none").equals("none") && !myPreferences.getString("passwd", "none").equals("none")){
            lanzarSplashActivity()
        }

    }

    fun setUI(){
        dni = findViewById(R.id.et_DependentDNI)
        passwd = findViewById(R.id.et_password)
    }

    fun clickEvent(v : View){

        //Falta una comprobación en la base de datos de si el usuario y contraseña es incorrecto o no

        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

        val editor = myPreferences.edit()

        editor.putString("dni", dni.text.toString())
        editor.putString("passwd", passwd.text.toString())

        editor.commit()

        lanzarSplashActivity()
    }

    fun lanzarSplashActivity(){
        var intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}