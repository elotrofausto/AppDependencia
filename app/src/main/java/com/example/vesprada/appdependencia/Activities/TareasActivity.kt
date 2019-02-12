package com.example.vesprada.appdependencia.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.vesprada.appdependencia.AdaptersAndClasses.Adapter_XAvisoModel
import com.example.vesprada.appdependencia.DB.DependenciaDBManager
import com.example.vesprada.appdependencia.Models.XAvisoModel
import com.example.vesprada.appdependencia.R
import kotlinx.android.synthetic.main.activity_red_button.*
import java.text.SimpleDateFormat
import java.util.*

class TareasActivity : AppCompatActivity(){

    var listaTareas : ArrayList<XAvisoModel> = ArrayList()
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : Adapter_XAvisoModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.emergencias -> {
                var intent = Intent(this, RedButtonActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.notificaciones -> {
                var intent = Intent(this, NotificacionesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.eventos -> {
                return@OnNavigationItemSelectedListener true
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
        setContentView(R.layout.activity_tareas)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.eventos

        initDB()
        setUI()

        //Este metodo lo he creado para que ese fragmento de codigo este separado para que en caso de moverlo solamente copiar el metodo entero
        //cargarPrimeraTarea()

    }

    private fun initDB() {
        var db = DependenciaDBManager(this.applicationContext)

        db.insertAviso(XAvisoModel(0, "123456789L", "medico", "Cita con el médico", Date(), Date(), "Mañana a las 8:00 AM"))
        listaTareas.addAll(db.avisoRows)
    }

    private fun cargarPrimeraTarea() {

        if (!listaTareas.isEmpty()){
            var date = listaTareas.get(0).fecDesde
            var df = SimpleDateFormat("d '/' MM '/' yyyy")
            var fechaFormateada = df.format(date)

            findViewById<TextView>(R.id.tvDescripcion).text = listaTareas.get(0).name
            findViewById<TextView>(R.id.tvDate).text = fechaFormateada

            when (listaTareas.get(0).tipo){
                "medicinas" ->  findViewById<ImageView>(R.id.ivCurrentIcon).setBackgroundResource(R.drawable.ic_medicine)
                "medico" ->  findViewById<ImageView>(R.id.ivCurrentIcon).setBackgroundResource(R.drawable.ic_medico)
                else ->  findViewById<ImageView>(R.id.ivCurrentIcon).setBackgroundResource(R.drawable.ic_otras_citas)
            }
        }

    }

    private fun setUI() {
        recyclerView = findViewById(R.id.lvTasks)
        adapter = Adapter_XAvisoModel(this, listaTareas, recyclerView, findViewById(R.id.lyCurrentTask))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun onClickBotonRojo(v: View){
        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
    }
}
