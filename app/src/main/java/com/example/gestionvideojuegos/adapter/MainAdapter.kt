package com.example.gestionvideojuegos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gestionvideojuegos.R

class MainAdapter(val context: Context, private var videojuegoList: MutableList<Videojuego>):
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    fun setListData(data:MutableList<Videojuego>){
        videojuegoList.clear()
        videojuegoList.addAll(data)
        notifyDataSetChanged()
    }

    fun updateVideogames(videojuegoList: List<Videojuego>){
        this.videojuegoList = videojuegoList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_videojuegos, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videojuegoList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val videojuego = videojuegoList[position]

        Glide.with(context).load(videojuego.foto).into(holder.circleImageView)
        holder.txt_title.text = videojuego.nombre
        holder.txt_desc.text = videojuego.descripcion
    }

    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val circleImageView: de.hdodenhof.circleimageview.CircleImageView = itemView.findViewById(R.id.circleImageView)
        val txt_title: android.widget.TextView = itemView.findViewById(R.id.txt_title)
        val txt_desc: android.widget.TextView = itemView.findViewById(R.id.txt_desc)

    }
}