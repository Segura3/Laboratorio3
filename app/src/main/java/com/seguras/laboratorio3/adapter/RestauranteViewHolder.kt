package com.seguras.laboratorio3.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.seguras.laboratorio3.databinding.RestaurantCardBinding
import com.seguras.laboratorio3.model.Restaurante
import com.squareup.picasso.Picasso

class RestauranteViewHolder(view: View): RecyclerView.ViewHolder(view)/*, View.OnClickListener*/ {
    private val binding = RestaurantCardBinding.bind(view)

    fun bind(item: Restaurante){
        with(binding){
            tvName.text = item.nombre
            tvCalificacion.text = "Calificación: "+item.calificacion
            tvCosto.text = "Costo promedio: "+item.costo
            tvFundacion.text = "Fundado desde "+item.ano
            Picasso.get().load(item.fotos[0]).into(ivThum)
        }
    }
}