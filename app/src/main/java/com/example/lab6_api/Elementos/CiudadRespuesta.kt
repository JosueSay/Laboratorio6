package com.example.lab6_api.Elementos

import com.google.gson.annotations.SerializedName
/**
 * Clase CiudadRespuesta
 *
 * Clase que defina el objeto respuesta que se obtendrá de la llamada para mapear la estructura del JSON que la API devuelve
 * */
class CiudadRespuesta {
    // Mapeo de la clave _links del JSON de la API
    @SerializedName("_links")
    var links: Links? = null

    // Mapeo de la clave ua:item del JSON de la API
    class Links {
        @SerializedName("ua:item")
        var cities: ArrayList<Ciudad>? = null
    }
}



