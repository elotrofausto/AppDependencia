package com.example.vesprada.appdependencia.AdaptersAndClasses

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.vesprada.appdependencia.R

import java.util.ArrayList

class adapter_tareas(private val contexto: Context, private val listItems: ArrayList<tareas_object>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    private var listener: View.OnClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        var contentView = LayoutInflater.from(contexto).inflate(R.layout.recycler_view_items, null)

        contentView.setOnClickListener(this)

        return holder(contentView)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        var version = listItems[i]
        var Holder = viewHolder as holder
        Holder.imagen.setImageResource(version.img)
        Holder.texto.text = version.descripcionPequenya
        Holder.hora.text = version.getHora()

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View) {
        if (listener != null) {
            if (listener != null) {
                listener!!.onClick(v)
            }
        }

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
