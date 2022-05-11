package com.seguras.laboratorio3.services

import com.seguras.laboratorio3.model.Restaurante
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getRestaurantes(@Url url: String): Response<ArrayList<Restaurante>>
}