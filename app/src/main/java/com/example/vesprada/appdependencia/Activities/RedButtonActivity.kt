package com.example.vesprada.appdependencia.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.vesprada.appdependencia.Background.Background
import com.example.vesprada.appdependencia.R
import kotlinx.android.synthetic.main.activity_red_button.*
import java.sql.DriverManager
import java.sql.SQLException

class RedButtonActivity : AppCompatActivity() {

    private var bg : Background = Background();

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

                var intent = Intent(this, TareasActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.mapa -> {

                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.configuracion -> {
                var intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        false
    }

    var PERMISSION_CALL : Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
    var REQUEST_CONTACTS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_red_button)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.emergencias
        bg.start();
        //actionBar.hide()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_CONTACTS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, R.string.contacts_read_right_required, Toast.LENGTH_LONG).show()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }


    fun onClickLlamadas(v: View){

        when(v.id){

            R.id.btnHospital -> {Log.i("LLAMANDO: ", "SE ESTA LLAMANDO AL HOSPITAL")}
            R.id.btnBomberos -> {Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A LOS BOMBEROS")}
            R.id.btnPolicia -> {Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A LA POLICÍA")}
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




}
