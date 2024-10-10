package com.example.desafio3dsm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CursoAdapter(private val cursos: List<Curso>) :
    RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    private var onItemClick: OnItemClickListener? = null

    class CursoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.tvTitulo)
        val resumenTextView: TextView = view.findViewById(R.id.tvResumen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]

        // Verifica si los datos no son nulos ni vacíos
        Log.d("CursoAdapter", "Título: ${curso.Titulo}, Resumen: ${curso.Resumen}")

        holder.tituloTextView.text = curso.Titulo
        holder.resumenTextView.text = curso.Resumen

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(curso)
        }


    }

    override fun getItemCount(): Int = cursos.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(curso: Curso)
    }
}
