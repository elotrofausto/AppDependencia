package com.example.vesprada.appdependencia.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.vesprada.appdependencia.DB.PostgresDBConnection
import com.example.vesprada.appdependencia.R
import java.sql.ResultSet
import java.sql.SQLException


class LoginActivity : AppCompatActivity() {

    private val MYPREFS = "MyPrefs"
    private val DNI = "dni"
    private val PASS = "passwd"
    private val NONE = "none"

    private lateinit var dni: EditText
    private lateinit var passwd : EditText
    private lateinit var loginTask: LoginTask
    companion object {
        var loginPb: ProgressBar? = null
        var tvlog: TextView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUI()
    }

    fun setUI(){
        dni = findViewById(R.id.et_DependentDNI)
        passwd = findViewById(R.id.et_password)
        loginPb = findViewById(R.id.loginPb)
        tvlog = findViewById(R.id.tvlog)
    }

    fun clickEvent(v : View){

        val myPreferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
        val editor = myPreferences.edit()

        editor.putString(DNI, dni.text.toString())
        editor.putString(PASS, passwd.text.toString())
        editor.commit()

        loginTask = LoginTask(dni.text.toString(), passwd.text.toString(), this)
        findViewById<ProgressBar>(R.id.loginPb).visibility = View.VISIBLE
        loginTask.execute()
    }

    fun testLogin(dni: String, pass: String){
        loginTask = LoginTask(dni, pass, this)
        loginTask.execute()
    }

    //static class for AsyncTask
    //----------------------------------------------------------------------------------------------
    class LoginTask(private val user: String, private val pass: String, private val context: Context) : AsyncTask<Void, Void, Boolean>() {
        private val MYPREFS = "MyPrefs"
        private lateinit var rs: ResultSet
        var correctLogin: Boolean = false
        val stsql = "SELECT id FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = '$user') AND password ='$pass'"

        init {
            this.correctLogin = false
        }

        //Runs in background Thread
        override fun doInBackground(vararg params: Void): Boolean? {
            try {
                val instance = PostgresDBConnection.getInstance()
                val conn = instance.connection
                val st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
                rs = st.executeQuery(stsql)
                correctLogin = rs.isBeforeFirst
                if (correctLogin){
                    val myPreferences = context.getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
                    val editor = myPreferences.edit()
                    editor.putString("ID", rs.getString(0))
                    editor.commit()
                }
                conn.close()
            } catch (se: SQLException) {
                println("oops! No se puede conectar. Error: " + se.toString())
            } catch (e: ClassNotFoundException) {
                println("oops! No se encuentra la clase. Error: " + e.message)
            }catch (e: Exception){
                println("oops! No hay conexión a la base de datos, se va a iniciar en modo sin conexión")
                val intent = Intent(context, OfflineRedButtonActivity::class.java)
                context.startActivity(intent)
            }

            return correctLogin

        }

        override fun onPreExecute() {
            super.onPreExecute()
            loginPb!!.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Boolean?) {
            loginPb!!.visibility = View.INVISIBLE
            if (result!!) {
                Toast.makeText(context, "AUTENTICACIÓN CORRECTA", Toast.LENGTH_LONG).show()
                tvlog!!.tag="LoginCorrect"
                lanzarSplashActivity()
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "ERROR DE AUTENTICACIÖN", Toast.LENGTH_LONG).show()
                tvlog!!.text = "LoginIncorrect"
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            Log.e("onCancelled", "ASYNCTASK " + this.javaClass.simpleName + ": I've been canceled and ready to GC clean")
        }

        private fun lanzarSplashActivity() {
            val intent = Intent(context, SplashLoadingActivity::class.java)
            context.startActivity(intent)
        }

    }
}