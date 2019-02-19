package com.example.vesprada.appdependencia.Adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.example.vesprada.appdependencia.R

class DialogoPersonalizadoDependiente : DialogFragment() {

    private var titulo : String = ""
    private var passwd : String = ""
    private var asistente = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val view = activity!!.layoutInflater.inflate(R.layout.layout_dialog_fragment, null)

        val edPasswd = view.findViewById<EditText>(R.id.et_checkPassword)

        titulo = getString(R.string.titulo_asistente)

        builder.setView(view)
                .setTitle(titulo)
                .setPositiveButton(getString(R.string.continuar), null)
                .setNegativeButton(getString(R.string.bt_cancel)) { dialog, which -> }

        val dialog = builder.create()

        dialog.setOnShowListener {
            val positivo = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positivo.setOnClickListener {
                if (edPasswd.text.toString().contentEquals(passwd)) {
                    Toast.makeText(activity, getString(R.string.correct_password), Toast.LENGTH_LONG).show()

                    //Segun si es asistente o no activa unos campos u otros
                    if(asistente){
                        activity!!.findViewById<View>(R.id.ed_ipAsistente).isEnabled=true
                    } else {
                        activity!!.findViewById<View>(R.id.ed_dniDependiente).isEnabled = true
                        activity!!.findViewById<View>(R.id.ed_PasswdDependiente).isEnabled = true
                    }
                    activity!!.findViewById<View>(R.id.btn_save).visibility = View.VISIBLE
                    activity!!.findViewById<View>(R.id.btn_cancelarCambios).visibility = View.VISIBLE
                    dismiss()
                } else {
                    Toast.makeText(activity, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show()
                }
            }
        }

        return dialog

    }

    fun DependienteOAsistente(int: Int, Passwd: String){

        passwd = Passwd

        when(int){
            1-> {

                asistente = false
            }
            2-> {

                asistente = true
            }
        }

    }
}
