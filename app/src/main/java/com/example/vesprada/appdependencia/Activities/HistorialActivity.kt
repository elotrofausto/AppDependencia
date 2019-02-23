package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import com.example.vesprada.appdependencia.Adapters.Adapter_XAvisoModel
import com.example.vesprada.appdependencia.DB.DependenciaDBContract
import com.example.vesprada.appdependencia.DB.DependenciaDBManager
import com.example.vesprada.appdependencia.Models.XAvisoModel
import com.example.vesprada.appdependencia.Models.XGeoModel
import com.example.vesprada.appdependencia.R
import com.example.vesprada.appdependencia.Utils.CreateRedButtonIntent
import kotlinx.android.synthetic.main.activity_historial.*
import java.util.*

class HistorialActivity : AppCompatActivity(){

    var listaTareas : ArrayList<XAvisoModel> = ArrayList()
    lateinit var db : DependenciaDBManager
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : Adapter_XAvisoModel
    private val MYPREFS = "MyPrefs"
    private val DAYNIGHT = "dayNight"
    private lateinit var preferences: SharedPreferences
    companion object {
        lateinit var currentId: Integer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_historial)
        navigationH.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationH.selectedItemId= R.id.eventos

        initDB()
        setUI()
        cambiarModoNocturnoDiurno(preferences.getBoolean(DAYNIGHT, true))
        cargarPrimeraTarea()
    }

    private fun initDB() {
        db = DependenciaDBManager(this.applicationContext)
        listaTareas.addAll(db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 1"))
        var listageo : ArrayList<XGeoModel> = ArrayList()
        listageo.addAll(db.getGeoRows(null))

    }

    private fun cambiarModoNocturnoDiurno(b: Boolean){

        if(b){
            findViewById<ConstraintLayout>(R.id.lyAvisos).background = getDrawable(R.drawable.patternbg)
        } else {
            findViewById<ConstraintLayout>(R.id.lyAvisos).background = getDrawable(R.drawable.patternbg_dark)
        }

    }

    private fun cargarPrimeraTarea() {
        if (!listaTareas.isEmpty()){
            if (!listaTareas.isEmpty()){
                findViewById<TextView>(R.id.tvDescripcion).text = listaTareas.get(0).name
                findViewById<TextView>(R.id.tvDate).text = listaTareas.get(0).fecDesde.toString()
                currentId = Integer(listaTareas.get(0).id)
                if (!findViewById<ToggleButton>(R.id.toggleHistButton).isChecked){
                    findViewById<Button>(R.id.btUndone).visibility = View.VISIBLE
                }
            }
        }else{
            findViewById<TextView>(R.id.tvDescripcion).text = getString(R.string.EmptyHistory)
            findViewById<TextView>(R.id.tvDate).text = getString(R.string.keepComing)
            findViewById<Button>(R.id.btUndone).visibility = View.INVISIBLE
        }
    }

    private fun setUI() {
        recyclerView = findViewById(R.id.lvTasks)
        adapter = Adapter_XAvisoModel(this, listaTareas, recyclerView, findViewById(R.id.lyAvisos))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        preferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

    }

    fun onClickBotonRojo(v: View){
        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        var createIntent = CreateRedButtonIntent(this, intent, "botonRojo")
        createIntent.lanzarActivity()
    }

    fun onToggleClick(v: View)
    {
        if (findViewById<ToggleButton>(R.id.toggleHistButton).isChecked){
            listaTareas.removeAll(listaTareas)
            listaTareas.addAll(db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 2"))
            findViewById<Button>(R.id.btUndone).visibility = View.INVISIBLE
        }else{
            listaTareas.removeAll(listaTareas)
            listaTareas.addAll(db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 1"))
            findViewById<Button>(R.id.btUndone).visibility = View.VISIBLE

        }
        adapter.notifyDataSetChanged()
        cargarPrimeraTarea()
    }

    fun unfinishTask(v: View){
        db.setAvisoUnfinished(currentId.toInt())
        val avisoPredicate = { a : XAvisoModel -> a.id === currentId.toInt() }
        listaTareas.removeIf(avisoPredicate)
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
