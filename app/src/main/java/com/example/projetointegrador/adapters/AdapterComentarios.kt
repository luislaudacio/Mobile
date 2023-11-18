package com.example.projetointegrador.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.R
import com.example.projetointegrador.models.Comentario

class AdapterComentarios(private val contexto: Context, private val comentarios: List<Comentario>):
    RecyclerView.Adapter<AdapterComentarios.MeuViewHolder>() {


    class MeuViewHolder(itemView: View, val contexto: Context) : RecyclerView.ViewHolder(itemView) {

        val txtUsuarioCom: TextView = itemView.findViewById(R.id.txtUsuarioCom)
        val txtComentario: TextView = itemView.findViewById(R.id.txtComentario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeuViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(contexto)
        var view = inflater.inflate(R.layout.lista_comentarios, parent, false)

        return MeuViewHolder(view, contexto)
    }

    override fun onBindViewHolder(holder: MeuViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.txtUsuarioCom.text = comentario.usuario
        holder.txtComentario.text = comentario.comentarioTexto
    }

    override fun getItemCount(): Int {
        return comentarios.size
    }


}