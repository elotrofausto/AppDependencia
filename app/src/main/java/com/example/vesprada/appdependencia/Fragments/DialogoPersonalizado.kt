package com.example.vesprada.appdependencia.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText

import com.example.vesprada.appdependencia.R

class DialogoPersonalizado : DialogFragment() {

    private var titulo : String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val view = activity!!.layoutInflater.inflate(R.layout.layout_dialog_fragment, null)

        titulo = getString(R.string.titulo_alert_dialog)

        builder.setView(view)
                .setTitle(titulo)
                .setPositiveButton(getString(R.string.alert_dialog_ok), null)

        val dialog = builder.create()

        dialog.setOnShowListener {
            val positivo = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positivo.setOnClickListener {
                dismiss()
            }
        }

        return dialog

    }
}
