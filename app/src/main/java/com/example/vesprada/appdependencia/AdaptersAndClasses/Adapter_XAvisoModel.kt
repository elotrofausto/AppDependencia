package com.example.vesprada.appdependencia.AdaptersAndClasses

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.vesprada.appdependencia.Models.XAvisoModel
import com.example.vesprada.appdependencia.R
import java.text.SimpleDateFormat
import java.util.*


class Adapter_XAvisoModel(context: Context, avisoList: ArrayList<XAvisoModel>, recyclerView: RecyclerView, selectedItemView: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private var context: Context
    private var avisoList: ArrayList<XAvisoModel>
    private var recyclerView: RecyclerView
    private var selectedItemView: View

    init{
        this.context = context
        this.avisoList = avisoList
        this.recyclerView = recyclerView
        this.selectedItemView = selectedItemView
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        var contentView = LayoutInflater.from(context).inflate(R.layout.recycler_view_items, null)
        contentView.setOnClickListener(this)
        return holder(contentView)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        val item = avisoList[i]
        var Holder = viewHolder as holder

        //Establecemos el icono dependiendo del tipo de aviso
        when (item.tipo){
            "medicinas" -> Holder.imagen.setImageResource(R.drawable.ic_medicine)
            "medico" -> Holder.imagen.setImageResource(R.drawable.ic_medico)
            else -> Holder.imagen.setImageResource(R.drawable.ic_otras_citas)
        }

        //Formateamos la fecha
        var date = item.fecDesde
                var df = SimpleDateFormat("d '/' MM '/' yyyy")
                var fechaFormateada = ""//df.format(date)

        Holder.texto.text = item.name
        Holder.hora.text = fechaFormateada

    }

    override fun getItemCount(): Int {
        return avisoList.size
    }

    override fun onClick(v: View?) {
        val itemPosition = recyclerView.getChildLayoutPosition(v!!)
        val item = avisoList.get(itemPosition)
        Toast.makeText(context, item.toString(), Toast.LENGTH_LONG).show()
        selectedItemView.findViewById<TextView>(R.id.tvDescripcion).text = item.name
        selectedItemView.findViewById<ImageView>(R.id.ivCurrentIcon).setImageDrawable(context.getDrawable(R.drawable.ic_otras_citas))
    }

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imagen: ImageView
        var texto: TextView
        var hora: TextView

        init {
            imagen = itemView.findViewById(R.id.ivIcono)
            texto = itemView.findViewById(R.id.tvLDesc)
            hora = itemView.findViewById(R.id.tvTime)
        }
    }

}
