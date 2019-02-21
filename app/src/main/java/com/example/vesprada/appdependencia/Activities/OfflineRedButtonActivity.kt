package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.vesprada.appdependencia.Fragments.DialogoPersonalizado
import com.example.vesprada.appdependencia.R

class OfflineRedButtonActivity : AppCompatActivity() {

    private val MYPREFS = "MyPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_red_button)

        //Muestra el dialogo
        val fm = supportFragmentManager
        var dialog = DialogoPersonalizado()

        dialog.show(fm, "")

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

    fun onClickBotonRojo(v: View){

        var pb : ActivityLlamada.PanicButtonTask = ActivityLlamada.PanicButtonTask(this, getSharedPreferences(MYPREFS, Context.MODE_PRIVATE))
        pb.execute()

        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)

    }
}
