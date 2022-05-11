package com.seguras.laboratorio3.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Restaurante(
    @SerializedName("ID")           var ID: String,
    @SerializedName("nombre")       var nombre: String,
    @SerializedName("ano")          var ano: String,
    @SerializedName("costo")        var costo: String,
    @SerializedName("calificacion") var calificacion: String,
    @SerializedName("resena")       var resena: String,
    @SerializedName("direccion")    var direccion: String,
    @SerializedName("fotos")        var fotos: ArrayList<String>,
    @SerializedName("cordenadas")   var cordenadas: List<Double>
    ): Serializable
