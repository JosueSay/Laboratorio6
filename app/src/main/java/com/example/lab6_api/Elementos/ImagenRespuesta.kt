package com.example.lab6_api.Elementos

import com.google.gson.annotations.SerializedName

/**
 * Clase ImagenRespuesta
 *
 * Clase que defina el objeto respuesta que se obtendrá de la llamada para mapear la estructura del JSON que la API devuelve
 * */
data class ImagenRespuesta(
    // Mapeo de la clave "photos" del JSON de la API
    @SerializedName("photos")
    val photos: List<Photo>
) {
    data class Photo(
        // Mapeo de la clave "image" del JSON de la API
        @SerializedName("image")
        val image: Image
    ) {
        data class Image(
            // Mapeo de la clave "mobile" del JSON de la API
            @SerializedName("mobile")
            val mobile: String
        )
    }
}

