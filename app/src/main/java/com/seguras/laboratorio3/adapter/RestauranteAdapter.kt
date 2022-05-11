package com.seguras.laboratorio3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seguras.laboratorio3.R
import com.seguras.laboratorio3.model.Restaurante

class RestauranteAdapter(val restaurantes: ArrayList<Restaurante>, private val onRestClickListener: OnRestClickListener): RecyclerView.Adapter<RestauranteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RestauranteViewHolder(layoutInflater.inflate(R.layout.restaurant_card, parent, false))
    }

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val item = restaurantes[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            onRestClickListener.onRestItemClicked(it, position)
        }
    }

    override fun getItemCount(): Int {
        return restaurantes.size
    }
}