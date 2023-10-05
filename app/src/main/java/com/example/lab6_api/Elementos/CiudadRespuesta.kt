package com.example.lab6_api.Elementos

import com.google.gson.annotations.SerializedName

class CiudadRespuesta {
    @SerializedName("_links")
    var links: Links? = null

    class Links {
        @SerializedName("ua:item")
        var cities: ArrayList<Ciudad>? = null
    }
}



