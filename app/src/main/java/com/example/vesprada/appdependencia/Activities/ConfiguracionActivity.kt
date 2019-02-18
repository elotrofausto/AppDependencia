package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.vesprada.appdependencia.AdaptersAndClasses.DialogoPersonalizadoDependiente
import com.example.vesprada.appdependencia.R
import com.example.vesprada.appdependencia.Utils.PdfFromXmlFile
import kotlinx.android.synthetic.main.activity_configuracion.*
import kotlinx.android.synthetic.main.app_bar_configuracion.*
import kotlinx.android.synthetic.main.content_configuracion.*
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.support.v4.content.ContextCompat.startActivity
import java.io.FileOutputStream


class ConfiguracionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val MYPREFS = "MyPrefs"
    private val DNI = "dni"

    lateinit var sharedPreferences: SharedPreferences

    lateinit var btnSave : Button
    lateinit var btnCancel : Button
    lateinit var etNombre: EditText
    lateinit var etPasswd: EditText
    lateinit var ipAsistente: EditText
    lateinit var ultimoNombre : String
    lateinit var ultimaPasswd : String
    lateinit var ultimaIpAsistente : String
    var editando: Boolean = false

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

                var intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.googlemap -> {

                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()

            }

            R.id.configuracion ->{
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)
        setSupportActionBar(toolbar)
        navigationconf.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigationconf.selectedItemId= R.id.configuracion

        fab.setOnClickListener { view ->
            var intent = Intent(this, ActivityLlamada::class.java);
            Log.i("LLAMANDO: ", "SE ESTA LLAMANDO A EMERGENCIAS")
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setUI()

        sharedPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

        if(savedInstanceState!=null){
            restoreUI(savedInstanceState)
        }
    }

    private fun setUI() {
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancelarCambios)
        etNombre = findViewById(R.id.ed_nombreDependiente)
        etPasswd = findViewById(R.id.ed_passwdDependiente)
        ipAsistente = findViewById(R.id.ed_ipAsistente)

        //Carga el nombre, contraseña e ip del asistente que tenia previamente
        cargarDatosIniciales()
    }

    private fun restoreUI(bundle: Bundle){
        etNombre.setText(bundle.getString("etNombreText"))
        etPasswd.setText(bundle.getString("etPasswdText"))
        ipAsistente.setText(bundle.getString("ipAsistenteText"))

        etNombre.isEnabled=bundle.getBoolean("etNombreEnable")
        etPasswd.isEnabled=bundle.getBoolean("etPasswdEnable")
        ipAsistente.isEnabled=bundle.getBoolean("ipAsistenteEnable")

        ultimoNombre = bundle.getString("ultimoNombre")
        ultimaPasswd = bundle.getString("ultimaPasswd")
        ultimaIpAsistente = bundle.getString("ultimaIpAsistente")

        btnSave.visibility=bundle.getInt("botonesVisibles")
        btnCancel.visibility=bundle.getInt("botonesVisibles")

    }

    private fun cargarDatosIniciales() {

        //rellenar los campos con los de la base de datos
        etNombre.setText("Nombre De Ejemplo")
        etPasswd.setText("Contraseña De Ejemplo")
        ipAsistente.setText("none")

        ultimoNombre=etNombre.text.toString()
        ultimaPasswd=etPasswd.text.toString()
        ultimaIpAsistente=ipAsistente.text.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putBoolean("etNombreEnable", etNombre.isEnabled)
        outState.putBoolean("etPasswdEnable", etPasswd.isEnabled)
        outState.putBoolean("ipAsistenteEnable", ipAsistente.isEnabled)

        outState.putInt("botonesVisibles", btnSave.visibility)

        outState.putString("etNombreText", etNombre.text.toString())
        outState.putString("etPasswdText",etPasswd.text.toString())
        outState.putString("ipAsistenteText", ipAsistente.text.toString())

        outState.putString("ultimoNombre", ultimoNombre)
        outState.putString("ultimaPasswd", ultimaPasswd)
        outState.putString("ultimaIpAsistente",ultimaIpAsistente)

        super.onSaveInstanceState(outState)
    }

    //Guarda los cambios
    fun GuardarOnClick(v: View){

        //Si se esta editando editando la ip del asistente...
        if(etNombre.isEnabled){
            //Aqui habría que poner que guarde los datos correspondientes
            ultimoNombre=etNombre.text.toString()
            ultimaPasswd=etPasswd.text.toString()
        }
        //Si se esta editando el nombre y contraseña...
        else {
            //Aqui habría que poner que guarde los datos correspondientes
            ultimaIpAsistente=ipAsistente.text.toString()

        }

        desacivarCampos()

    }

    fun CancelarOnClick(v: View){

        etNombre.setText(ultimoNombre)
        etPasswd.setText(ultimaPasswd)
        ipAsistente.setText(ultimaIpAsistente)

        desacivarCampos()

    }

    private fun desacivarCampos() {
        btnSave.visibility=View.GONE
        btnCancel.visibility=View.GONE
        ed_nombreDependiente.isEnabled=false
        ed_passwdDependiente.isEnabled=false
        ipAsistente.isEnabled=false
        editando=false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.configuracion, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_mod_datos->{

                //comprueba que no le haya dado antes a editar
                if(!ed_nombreDependiente.isEnabled) {

                    //Muestra el dialogo que pide la contraseña del dependiente
                    val fm = supportFragmentManager
                    var dialog = DialogoPersonalizadoDependiente()

                    //Para que active los campos de edición del dependiente hay que pasar un '1' como primer paramentro

                    dialog.DependienteOAsistente(1, "1234")
                    dialog.show(fm, "Patata")

                } else {
                    Toast.makeText(this, getString(R.string.ya_esta_editando), Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_mod_ip_asistente->{
                //comprueba que no le haya dado antes a editar
                if(!ipAsistente.isEnabled) {
                    //Muestra el dialogo que pide la contraseña del dependiente
                    val fm = supportFragmentManager
                    var dialog = DialogoPersonalizadoDependiente()

                    //Para que active los campos de edición del dependiente hay que pasar un '1' como primer paramentro

                    dialog.DependienteOAsistente(2, "4321")
                    dialog.show(fm, "Patata")
                } else {
                    Toast.makeText(this, getString(R.string.ya_esta_editando), Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_informe_medicamentos->{
                Toast.makeText(this, "Imprimendo informe medicamentos", Toast.LENGTH_LONG).show()

                val pdfFromXmlFile = PdfFromXmlFile(1, sharedPreferences.getString(DNI, "none"))
                val pdfIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfFromXmlFile.url.toString()))
                startActivity(pdfIntent)

            }
            R.id.nav_informe_otras_tareas->{
                Toast.makeText(this, "Imprimendo informe de otras tareas", Toast.LENGTH_LONG).show()

                val pdfFromXmlFile = PdfFromXmlFile(2, sharedPreferences.getString(DNI, "none"))
                val pdfIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfFromXmlFile.url.toString()))
                startActivity(pdfIntent)

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
