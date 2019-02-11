package com.example.vesprada.appdependencia.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.View
import android.view.Window
import com.example.vesprada.appdependencia.R
import kotlinx.android.synthetic.main.activity_red_button.*

class NotificacionesActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.emergencias -> {

                var intent = Intent(this, RedButtonActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.notificaciones -> {
                return@OnNavigationItemSelectedListener true

            }
            R.id.eventos -> {

                var intent = Intent(this, TareasActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.mapa -> {

                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_notificaciones)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.notificaciones
    }

    fun onClickBotonRojo(v: View){
        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)
    }
}
