package com.example.vesprada.appdependencia.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.example.vesprada.appdependencia.R
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*

class ActivityLlamada : AppCompatActivity() {

    private val MYPREFS = "MyPrefs"
    lateinit var barra : SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llamada)
        setUI()
        var pButton = PanicButtonTask(this, getSharedPreferences(MYPREFS, Context.MODE_PRIVATE))
        pButton.execute()
        Toast.makeText(this, getString(R.string.llamandoEmergencias), Toast.LENGTH_LONG).show()

    }

    private fun esperar(i: Long) {

        var handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run(){


                llamar()

            }
        },i)

    }


    @SuppressLint("MissingPermission")
    private fun llamar() {

        if(barra.progress!=0) {

            Toast.makeText(this, getString(R.string.llamandoEmergencias), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.llamadaCancelada), Toast.LENGTH_SHORT).show()
            finish()

        }

    }

    private fun setUI() {
        barra = findViewById(R.id.barraCancelar)
    }

    //    static class for AsyncTask
    //--------------------------------------------------------------------------------------------------
    class PanicButtonTask(caller: Activity, private val sharedPreferences: SharedPreferences) : AsyncTask<Void, Void, Boolean>() {


        //Weak reference
        private val activityWeakReference: WeakReference<Activity>

        private lateinit var clientSocket: Socket

        init {
            activityWeakReference = WeakReference(caller)
        }

        //Runs in background Thread
        override fun doInBackground(vararg params: Void): Boolean? {
            //TODO conectar mediante socket a Asistente asignado o a todos progresivamente
            try {
                val inet = InetSocketAddress("172.16.226.14", PORT)
                clientSocket = Socket()
                clientSocket.soTimeout = TIMEOUT
                clientSocket.connect(inet)

                val jsonAlarm = createJson()

                var reply = ""

                val bw = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
                val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

                do {

                    bw.write(jsonAlarm.toString())
                    bw.newLine()
                    bw.flush()

                    reply = br.readLine()
                    Log.i("SOCKET", "Respuesta recibida")

                } while (reply != CORRECT_STATUS)
                Log.i("SOCKET", "Alarma aceptada")
                clientSocket!!.close()

            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            return true
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Boolean?) {
            //TODO darle confirmación de la ayuda al dependiente
        }


        override fun onCancelled() {
            super.onCancelled()
            //TODO desactivar alarma
        }

        @Throws(JSONException::class)
        private fun createJson(): JSONObject {
            val jsonAlarm = JSONObject()
            jsonAlarm.accumulate("idDependiente", sharedPreferences.getString(DNI, NONE))
            jsonAlarm.accumulate("fecha", Date().time)
            return jsonAlarm
        }

        companion object {

            private val PORT = 4444
            private val TIMEOUT = 35000
            private val DNI = "dni"
            private val NONE = "none"
            private val CORRECT_STATUS = "1"
        }
    }
}
