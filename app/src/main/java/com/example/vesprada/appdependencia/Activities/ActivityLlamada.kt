package com.example.vesprada.appdependencia.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.example.vesprada.appdependencia.DB.PostgresDBConnection
import com.example.vesprada.appdependencia.R
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException
import java.sql.ResultSet
import java.util.*

class ActivityLlamada : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private val TIPOLLAMADA = "tipoLlamada"
    private val MYPREFS = "MyPrefs"
    lateinit var barra : SeekBar
    lateinit var pButton: PanicButtonTask
    companion object {
        lateinit var tipo: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llamada)
        setUI()
        tipo = intent.extras.getString(TIPOLLAMADA)
        pButton = PanicButtonTask(this, getSharedPreferences(MYPREFS, Context.MODE_PRIVATE))
        pButton.execute()
        Toast.makeText(this, getString(R.string.llamandoEmergencias), Toast.LENGTH_LONG).show()
    }

    private fun setUI() {
        barra = findViewById(R.id.barraCancelar)
        barra.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(progress==0){
            Toast.makeText(this, getString(R.string.llamadaCancelada), Toast.LENGTH_SHORT).show()
            if (pButton != null && !pButton.isCancelled){
                pButton.cancel(true)
            }
            finish()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Nothing happens
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Nothing happens
    }

    //    static class for AsyncTask
    //--------------------------------------------------------------------------------------------------
    class PanicButtonTask(caller: Activity, private val sharedPreferences: SharedPreferences) : AsyncTask<Void, Void, Boolean>() {


        //Weak reference
        private val activityWeakReference: WeakReference<Activity> = WeakReference(caller)
        private val GET_IPS = "SELECT DISTINCT ip from x_ip_model"
        private val ASISTENTE = "ipasistente"
        private val NONE = "none"

        private lateinit var clientSocket: Socket

        //Runs in background Thread
        override fun doInBackground(vararg params: Void): Boolean? {

                val jsonAlarm = createJson()
                var result = false

            //Si no puede conectar con su asistente asignado prueba con todos los asistentes logueados
                result = conectarAsignado(jsonAlarm)
                if (!result){
                    result = conectarProgresivamente(jsonAlarm)
                }

            return result
        }


        private fun conectarAsignado(jsonAlarm: JSONObject): Boolean {
            var reply = ""
            val ip = sharedPreferences.getString(ASISTENTE,NONE)
            if (ip != NONE){
                val inet = InetSocketAddress(ip, PORT)
                clientSocket = Socket()
                clientSocket.soTimeout = TIMEOUT
                clientSocket.connect(inet)
                val bw = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
                val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

                bw.write(jsonAlarm.toString())
                bw.newLine()
                bw.flush()

                reply = br.readLine()
                Log.i("SOCKET", "Respuesta recibida")

                br.close()
                bw.close()
                clientSocket.close()
            }

            return reply == CORRECT_STATUS

        }

        private fun conectarProgresivamente(jsonAlarm: JSONObject): Boolean {
            var listaIpActivas :List<String> = ArrayList()
            var reply = ""

            try{
            listaIpActivas = obtenerIpAsistentes()
            var count = 0
            do {
                var ip = listaIpActivas.get(count)

                val inet = InetSocketAddress(ip, PORT)
                clientSocket = Socket()
                clientSocket.soTimeout = TIMEOUT
                clientSocket.connect(inet)
                val bw = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
                val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

                bw.write(jsonAlarm.toString())
                bw.newLine()
                bw.flush()

                reply = br.readLine()
                Log.i("SOCKET", "Respuesta recibida")

                br.close()
                bw.close()
                clientSocket.close()

                if (count == listaIpActivas.size - 1){
                    count = 0
                }

            } while (reply != CORRECT_STATUS)
            Log.i("SOCKET", "Alarma aceptada")
            clientSocket!!.close()



        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception){
            Log.i("SOCKET",e.message)
        }
            return reply == CORRECT_STATUS
    }

        private fun obtenerIpAsistentes(): List<String> {

            var listaIpActivas :MutableList<String> = ArrayList()
            val instance = PostgresDBConnection.getInstance()
            val conn = instance.connection
            val st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
            var rs: ResultSet = st.executeQuery(GET_IPS)
            var correctLogin = rs.isBeforeFirst

            if (correctLogin){
                while (rs.next()){
                    listaIpActivas.add(rs.getString(rs.findColumn("ip")))
                }
            }

            return listaIpActivas

        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!){
                Log.i("SOCKET", "LA AYUDA ESTÁ EN CAMINO")
            }else{
                Log.i("SOCKET", "No se ha podido conectar")
            }
        }


        override fun onCancelled() {
            super.onCancelled()
            //TODO desactivar alarma
        }

        @Throws(JSONException::class)
        private fun createJson(): JSONObject {
            val jsonAlarm = JSONObject()
            jsonAlarm.accumulate("idDependiente", sharedPreferences.getString(DNI, NONE))
            jsonAlarm.accumulate("tipo", tipo)
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
