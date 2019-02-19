package com.example.vesprada.appdependencia.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.vesprada.appdependencia.Adapters.DialogoPersonalizadoDependiente
import com.example.vesprada.appdependencia.R
import com.example.vesprada.appdependencia.Utils.PdfFromXmlFile
import kotlinx.android.synthetic.main.activity_configuracion.*
import kotlinx.android.synthetic.main.app_bar_configuracion.*
import kotlinx.android.synthetic.main.content_configuracion.*


class ConfiguracionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val MYPREFS = "MyPrefs"
    private val DNI = "dni"
    private val PASS = "passwd"
    private val ASISTENTE = "ipasistente"

    private var passwdVisible=false

    lateinit var sharedPreferences: SharedPreferences

    lateinit var btnSave : Button
    lateinit var btnCancel : Button
    lateinit var etDNI: EditText
    lateinit var etPasswd: EditText
    lateinit var ipAsistente: EditText
    lateinit var ultimaIpAsistente : String

    var input_type : Int = 0
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

        sharedPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)

        setUI()

        if(savedInstanceState!=null){
            restoreUI(savedInstanceState)
        }
    }

    private fun setUI() {
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancelarCambios)
        etDNI = findViewById(R.id.ed_dniDependiente)
        etPasswd = findViewById(R.id.ed_PasswdDependiente)
        ipAsistente = findViewById(R.id.ed_ipAsistente)

        input_type = etPasswd.inputType

        //Carga el nombre, contraseña e ip del asistente que tenia previamente
        cargarDatosIniciales()
    }

    private fun restoreUI(bundle: Bundle){

        ipAsistente.setText(bundle.getString("ipAsistenteText"))

        ipAsistente.isEnabled=bundle.getBoolean("ipAsistenteEnable")

        ultimaIpAsistente = bundle.getString("ultimaIpAsistente")

        btnSave.visibility=bundle.getInt("botonesVisibles")
        btnCancel.visibility=bundle.getInt("botonesVisibles")

    }

    private fun cargarDatosIniciales() {

        //rellenar los campos con los de la base de datos
        etDNI.setText(sharedPreferences.getString(DNI, "00000000a"))
        etPasswd.setText(sharedPreferences.getString(PASS, "1234"))
        ipAsistente.setText(sharedPreferences.getString(ASISTENTE,""))

        if(ipAsistente.text.toString().length==0){
            btnSave.visibility=View.VISIBLE
            Toast.makeText(this, getString(R.string.no_hay_ip_asistente),Toast.LENGTH_LONG).show()
            ipAsistente.isEnabled=true
        }

        ultimaIpAsistente=ipAsistente.text.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putBoolean("ipAsistenteEnable", ipAsistente.isEnabled)

        outState.putInt("botonesVisibles", btnSave.visibility)

        outState.putString("ipAsistenteText", ipAsistente.text.toString())

        outState.putString("ultimaIpAsistente",ultimaIpAsistente)

        super.onSaveInstanceState(outState)
    }

    //Guarda los cambios
    fun GuardarOnClick(v: View){

        //Aqui habría que poner que guarde los datos correspondientes
        ultimaIpAsistente=ipAsistente.text.toString()
        val editor = sharedPreferences.edit()
        editor.putString(ASISTENTE, ipAsistente.text.toString())
        editor.commit()

        ipAsistente.isEnabled = false
        btnSave.visibility = View.GONE

    }

    //------------------------------------
    //Puede ser que esto haya que borrarlo
    //------------------------------------
    fun CancelarOnClick(v: View){

        ipAsistente.setText(ultimaIpAsistente)

        desacivarCampos()

    }

    private fun desacivarCampos() {
        btnSave.visibility=View.GONE
        btnCancel.visibility=View.GONE
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
                if(!passwdVisible) {

                    etPasswd.inputType =  InputType.TYPE_CLASS_TEXT
                    passwdVisible = true

                } else {

                    etPasswd.inputType =  input_type

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
