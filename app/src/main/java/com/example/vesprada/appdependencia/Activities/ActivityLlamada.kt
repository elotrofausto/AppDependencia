package com.example.vesprada.appdependencia.Activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.Toast
import com.example.vesprada.appdependencia.R

class ActivityLlamada : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {


    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        if(progress==0){
            Toast.makeText(this, getString(R.string.llamadaCancelada), Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    lateinit var barra : SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llamada)

        setUI()

        //esperar(5000)
        //Toast.makeText(this, getString(R.string.llamandoEmergencias), Toast.LENGTH_LONG).show()


    }

    private fun setUI() {
        barra = findViewById(R.id.barraCancelar)

        /*
        *
        * LISTENER A AÑADIR
        *
        * */

        barra.setOnSeekBarChangeListener(this)
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


}

