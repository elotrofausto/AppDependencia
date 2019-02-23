package com.example.vesprada.appdependencia.Activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initsplash)
        createChannel()
        comprobarInicio()
    }


    fun comprobarInicio(){
        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
        if(!myPreferences.getString(DNI, NONE).equals(NONE) && !myPreferences.getString(PASS, NONE).equals(NONE)){
            loginTask = LoginTask(myPreferences.getString(DNI,NONE), myPreferences.getString(PASS,NONE), this)
            loginTask.execute()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_id_channel)
            val description = getString(R.string.notification_id_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.notification_id_channel), name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

    }

    //static class for AsyncTask
    //----------------------------------------------------------------------------------------------
    class LoginTask(private val user: String, private val pass: String, private val context: Context) : AsyncTask<Void, Void, Boolean>() {
        private val MYPREFS = "MyPrefs"
        private lateinit var rs: ResultSet
        var correctLogin: Boolean = false

        init {
            this.correctLogin = false
        }

        //Runs in background Thread
        override fun doInBackground(vararg params: Void): Boolean? {
            try {
                val instance = PostgresDBConnection.getInstance()
                val conn = instance.connection
                val stsql = "SELECT id FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = '$user') AND password ='$pass'"
                val st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
                rs = st.executeQuery(stsql)
                correctLogin = rs.isBeforeFirst
                if (correctLogin){
                    rs.next()
                    val myPreferences = context.getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
                    val editor = myPreferences.edit()
                    editor.putInt("ID", rs.getInt(1))
                    editor.commit()
                }
                conn.close()
            } catch (se: SQLException) {
                println("oops! No se puede conectar. Error: " + se.toString())
            } catch (e: ClassNotFoundException) {
                println("oops! No se encuentra la clase. Error: " + e.message)
            } catch (e: Exception){
                println("oops! No hay conexión a la base de datos, se va a iniciar en modo sin conexión")
                val intent = Intent(context, OfflineRedButtonActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            }

            return correctLogin

        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                Toast.makeText(context, "AUTENTICACIÓN CORRECTA", Toast.LENGTH_LONG).show()
                lanzarSplashActivity()
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "ERROR DE AUTENTICACIÖN", Toast.LENGTH_LONG).show()
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
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


    }


}

