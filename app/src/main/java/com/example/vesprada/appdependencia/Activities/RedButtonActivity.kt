package com.example.vesprada.appdependencia.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.vesprada.appdependencia.Background.PanicButton
import com.example.vesprada.appdependencia.Background.SaveLocationService
import com.example.vesprada.appdependencia.R
import kotlinx.android.synthetic.main.activity_red_button.*


class RedButtonActivity : AppCompatActivity() {

    private val REQUEST_LOCATION_PERMISSION = 1234
    private val REQUEST_CONTACTS = 1
    private val MYPREFS = "MyPrefs"
    private val JOB_ID = 1235
    private val PERIOD_MS: Long = 1000 * 60 * 10
    private val DAYNIGHT = "dayNight"
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_red_button)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().build())
        setUI()
        cambiarModoNocturnoDiurno(preferences.getBoolean(DAYNIGHT, true))
        getPermissions()
        initJob()
    }

    fun setUI(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.emergencias
        preferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
    }
    fun cambiarModoNocturnoDiurno(b: Boolean) : Boolean{

        if(b){
            findViewById<ConstraintLayout>(R.id.container).background = getDrawable(R.drawable.patternbg)
            return true
        } else {
            findViewById<ConstraintLayout>(R.id.container).background = getDrawable(R.drawable.patternbg_dark)
            return false
        }

    }

    fun getPermissions(){
        if ((ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION)
        }
        else
        {
            Log.d("GEOLOCATION PERMISSION", "startTrackingLocation: permissions granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        run { when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    initJob()
                }
                else{
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show()
                }
            REQUEST_CONTACTS ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, R.string.contacts_read_right_required, Toast.LENGTH_LONG).show()
                }
            else ->
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } }

    }

    private fun initJob() {
            val serviceComponent = ComponentName(this, SaveLocationService::class.java)
            val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(JobInfo.Builder(JOB_ID, serviceComponent)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setMinimumLatency(1000)
                    .setPersisted(true)
                    .build())
    }

    fun onClickLlamadas(v: View){

        when(v.id){

            R.id.btnHospital -> {
                var intent = Intent(this, ActivityLlamada::class.java);
                Log.i("LLAMANDO: ", "SE ESTA LLAMANDO AL HOSPITAL")
                startActivity(intent)
            }
            R.id.btnBomberos -> {
                var intent = Intent(this, ActivityLlamada::class.java);
                Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A LOS BOMBEROS")
                startActivity(intent)
            }
            R.id.btnPolicia -> {
                var intent = Intent(this, ActivityLlamada::class.java);
                Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A LA POLICÍA")
                startActivity(intent)
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun iniciarLlamada(telefono : String){
        var intent = Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(telefono))
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)
    }

    fun onClickBotonRojo(v: View){
            var intent = Intent(this, ActivityLlamada::class.java);
            Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
            startActivity(intent)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.emergencias -> {
                return@OnNavigationItemSelectedListener true
            }

            R.id.notificaciones -> {

                var intent = Intent(this, NotificacionesActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.eventos -> {

                var intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.googlemap -> {

                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.configuracion -> {
                var intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                //finish()
            }
        }
        false
    }


}
