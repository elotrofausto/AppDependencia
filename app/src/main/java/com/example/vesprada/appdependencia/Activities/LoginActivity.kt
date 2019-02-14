package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.vesprada.appdependencia.Background.AsyncTasks
import com.example.vesprada.appdependencia.Background.BackgroundLogin
import com.example.vesprada.appdependencia.R

class LoginActivity : AppCompatActivity() {

    private val MYPREFS = "MyPrefs"
    private val DNI = "dni"
    private val PASS = "passwd"
    private val NONE = "none"

    private lateinit var dni: EditText
    private lateinit var passwd : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUI()

        conprobarInicio()
    }

    private fun conprobarInicio() {

        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

        if(!myPreferences.getString(DNI, NONE).equals(NONE) && !myPreferences.getString(PASS, NONE).equals(NONE)){
            var bgLogin = BackgroundLogin(myPreferences.getString(DNI, NONE), myPreferences.getString(PASS, NONE))
            bgLogin.start()
        }

    }

    fun setUI(){
        dni = findViewById(R.id.et_DependentDNI)
        passwd = findViewById(R.id.et_password)
    }

    fun clickEvent(v : View){

        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
        val editor = myPreferences.edit()

        editor.putString(DNI, dni.text.toString())
        editor.putString(PASS, passwd.text.toString())

        var loginTask: AsyncTasks.LoginTask = AsyncTasks.LoginTask(dni.text.toString(), passwd.text.toString())
        loginTask.execute()
        //TODO pasar context y load a asynctask

    }

    fun lanzarSplashActivity(){
        var intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}