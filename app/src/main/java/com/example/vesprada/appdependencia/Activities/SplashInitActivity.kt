package com.example.vesprada.appdependencia.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.vesprada.appdependencia.DB.PostgresDBConnection
import com.example.vesprada.appdependencia.R
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException


class SplashInitActivity : AppCompatActivity() {

    private val JOB_ID = 0
    private val MYPREFS = "MyPrefs"
    private val DNI = "dni"
    private val PASS = "passwd"
    private val NONE = "none"

    private lateinit var loginTask: LoginTask

    companion object {
        var splashPb: ProgressBar? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initsplash)

        setUI()
        comprobarInicio()
    }

    fun setUI(){
        splashPb = findViewById(R.id.splashPb)
    }

    fun comprobarInicio(){
        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
        if(!myPreferences.getString(DNI, NONE).equals(NONE) && !myPreferences.getString(PASS, NONE).equals(NONE)){
            loginTask = LoginTask(myPreferences.getString(DNI,NONE), myPreferences.getString(PASS,NONE), this)
            loginTask.execute()
            //TODO login only one time
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //static class for AsyncTask
    //----------------------------------------------------------------------------------------------
    class LoginTask(private val user: String, private val pass: String, private val context: Context) : AsyncTask<Void, Void, Boolean>() {

        var correctLogin: Boolean = false

        init {
            this.correctLogin = false
        }

        //Runs in background Thread
        override fun doInBackground(vararg params: Void): Boolean? {
            try {
                val instance = PostgresDBConnection.getInstance()
                val conn: Connection = instance.connection
                val stsql = "SELECT * FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = '$user') AND password ='$pass'"
                val st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
                val rs = st.executeQuery(stsql)
                correctLogin = rs.isBeforeFirst
                conn.close()
            } catch (se: SQLException) {
                println("oops! No se puede conectar. Error: " + se.toString())
            } catch (e: ClassNotFoundException) {
                println("oops! No se encuentra la clase. Error: " + e.message)
            }

            return correctLogin

        }

        override fun onPreExecute() {
            super.onPreExecute()
            SplashInitActivity.splashPb!!.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Boolean?) {
            SplashInitActivity.splashPb!!.visibility = View.INVISIBLE
            if (result!!) {
                Toast.makeText(context, "AUTENTICACIÓN CORRECTA", Toast.LENGTH_LONG).show()
                lanzarSplashActivity()
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "INTRODUZCA USUARIO Y CONTRASEÑA", Toast.LENGTH_LONG).show()
                lanzarLogin()
                (context as Activity).finish()
            }
        }


        override fun onCancelled() {
            super.onCancelled()
            Log.e("onCancelled", "ASYNCTASK " + this.javaClass.simpleName + ": I've been canceled and ready to GC clean")
        }

        private fun lanzarSplashActivity() {
            val intent = Intent(context, SplashLoadingActivity::class.java)
            context.startActivity(intent)
        }

        private fun lanzarLogin(){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

    }
}
