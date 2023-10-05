package com.example.lab6_api.CityApi

import com.example.lab6_api.Elementos.CiudadRespuesta
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interfaz CiudadApiService
 *
 * Tiene la funcionalidad de definir una función para hacer la petición a una API
 * */
interface CiudadApiService {
    @GET("urban_areas") // Petición GET con el endpoint "urban_areas"
    /**
     * Función obtenerListaCiudades
     *
     * Tiene la funcionalidad de retornar una instancia Call de un json adaptable a la clase CiudadRespuesta
     * */
    fun obtenerListaCiudades(): Call<CiudadRespuesta?>?

}