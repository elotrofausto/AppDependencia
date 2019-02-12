package com.example.vesprada.appdependencia.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.example.vesprada.appdependencia.R
import com.example.vesprada.appdependencia.AdaptersAndClasses.adapter_tareas
import com.example.vesprada.appdependencia.AdaptersAndClasses.tareas_object
import kotlinx.android.synthetic.main.activity_red_button.*
import java.text.SimpleDateFormat
import java.util.*

class TareasActivity : AppCompatActivity(){

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

    var listaTareas : ArrayList<tareas_object> = ArrayList()
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : adapter_tareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_tareas)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.eventos


        llenarLista()

        setUI()

        //Este metodo lo he creado para que ese fragmento de codigo este separado para que en caso de moverlo solamente copiar el metodo entero
        cargarPrimeraTarea()

    }

    private fun cargarPrimeraTarea() {

        var date = listaTareas.get(0).fecha
        var df = SimpleDateFormat("d '/' MM '/' yyyy")
        var fechaFormateada = df.format(date)

        findViewById<TextView>(R.id.tvDescripcion).text = listaTareas.get(0).descripcionCompleta
        findViewById<TextView>(R.id.tvDate).text = fechaFormateada

        findViewById<ImageView>(R.id.ivCurrentIcon).setBackgroundResource(listaTareas.get(0).img)
    }

    private fun setUI() {
        recyclerView = findViewById(R.id.lvTasks)
        adapter = adapter_tareas(this, listaTareas)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter.setOnClickListener((object : View.OnClickListener {
            override fun onClick(v: View) {
                var date = listaTareas.get(recyclerView.getChildAdapterPosition(v)).fecha
                var df = SimpleDateFormat("d '/' MM '/' yyyy")
                var fechaFormateada = df.format(date)

                findViewById<TextView>(R.id.tvDescripcion).text = listaTareas.get(recyclerView.getChildAdapterPosition(v)).descripcionCompleta
                findViewById<TextView>(R.id.tvDate).text = fechaFormateada

                findViewById<ImageView>(R.id.ivCurrentIcon).setBackgroundResource(listaTareas.get(recyclerView.getChildAdapterPosition(v)).img)
            }
        }))
        recyclerView.adapter=adapter

    }

    fun onClickBotonRojo(v: View){
        var intent = Intent(this, ActivityLlamada::class.java);
        Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
        startActivity(intent)
    }

    fun llenarLista() {

        listaTareas.add(tareas_object(R.drawable.ic_medico, "Ir al medico", Date(), "Consulta con el médico del corazón"))
        listaTareas.add(tareas_object(R.drawable.ic_otras_citas, "Ir al mercado", Date(), "Regatear por unos churros"))
        listaTareas.add(tareas_object(R.drawable.ic_medicine, "Tomar partilla roja", Date(), "Convertirse en neo"))
        listaTareas.add(tareas_object(R.drawable.ic_medico, "Ir al medico", Date(), "Consulta con el médico de la prostata"))
        listaTareas.add(tareas_object(R.drawable.ic_otras_citas, "Votar a VOX", Date(), "Abajo esta marruecos y Arriba España"))
        listaTareas.add(tareas_object(R.drawable.ic_otras_citas, "Jugar a la petanca", Date(), "Ir con Paco y Pepe a petanquear"))
        listaTareas.add(tareas_object(R.drawable.ic_otras_citas, "Buscar hueco", Date(), "Reservar plaza en el cementerio"))


    }
}
