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
import android.widget.TextView
import android.widget.ToggleButton
import com.example.vesprada.appdependencia.Adapters.Adapter_XAvisoModel
import com.example.vesprada.appdependencia.DB.DependenciaDBContract
import com.example.vesprada.appdependencia.DB.DependenciaDBManager
import com.example.vesprada.appdependencia.Models.XAvisoModel
import com.example.vesprada.appdependencia.R
import com.example.vesprada.appdependencia.R.id.tvDate
import com.example.vesprada.appdependencia.R.id.tvDescripcion
import kotlinx.android.synthetic.main.activity_red_button.*
import java.util.*

class HistorialActivity : AppCompatActivity(){

    var listaTareas : ArrayList<XAvisoModel> = ArrayList()
    lateinit var db : DependenciaDBManager
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : Adapter_XAvisoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_historial)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.eventos

        initDB()
        setUI()
        cargarPrimeraTarea()
    }

    private fun initDB() {
        db = DependenciaDBManager(this.applicationContext)
        listaTareas.addAll(db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 1"))
    }

    private fun cargarPrimeraTarea() {
        if (!listaTareas.isEmpty()){
            if (!listaTareas.isEmpty()){
                findViewById<TextView>(tvDescripcion).text = listaTareas.get(0).name
                findViewById<TextView>(tvDate).text = listaTareas.get(0).fecDesde.toString()
            }
        }else{
            findViewById<TextView>(tvDescripcion).text = "History is empty"
            findViewById<TextView>(tvDate).text = "Keep coming back for updates!"
        }
    }

    private fun setUI() {
        recyclerView = findViewById(R.id.lvTasks)
        adapter = Adapter_XAvisoModel(this, listaTareas, recyclerView, findViewById(R.id.lyAvisos))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun onClickBotonRojo(v: View){
        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)
    }

    public fun onToggleClick(v: View)
    {
        if (findViewById<ToggleButton>(R.id.toggleButton).isChecked){
            listaTareas.removeAll(listaTareas)
            listaTareas.addAll(db.getAvisoRows(
                    DependenciaDBContract.Aviso.TIPO + " = 'medicinas' AND "
                            + DependenciaDBContract.Aviso.FINALIZADO + " = 1"))
        }else{
            listaTareas.removeAll(listaTareas)
            listaTareas.addAll(db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 1"))
        }
        adapter.notifyDataSetChanged()
        cargarPrimeraTarea()
    }

    override fun onResume() {
        super.onResume()
    }

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
            R.id.googlemap -> {
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.configuracion -> {
                var intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        false
    }
}
