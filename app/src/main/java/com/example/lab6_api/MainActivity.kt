package com.example.lab6_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.lab6_api.CityApi.CiudadApiService
import com.example.lab6_api.Elementos.Ciudad
import com.example.lab6_api.Elementos.CiudadRespuesta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var retrofit: Retrofit? = null                      // Instancia de Retrofit
    private val TAG = "CIUDADES"                                // Etiqueta de depuración
    private var urlBase = "https://api.teleport.org/api/"       // Url base de la API
    private lateinit var citySpinner: Spinner                   // Espiner con el nombre de las ciudades


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Spinner
        citySpinner = findViewById(R.id.citySpinner)
        // Instanciar retrofit
        retrofit = Retrofit
            .Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Función para obtener datos de la API
        obtenerDatos()
    }

    fun obtenerDatos() {
        // Instancia de CiudadApiService para el uso de retrofit para realizar petición de API
        var service: CiudadApiService = retrofit!!.create(CiudadApiService::class.java)
        // Instancia Call con el mapeado del retorno de función "obtenerLisaCiudades"
        var ciudadRespuestaCall: Call<CiudadRespuesta?>? = service.obtenerListaCiudades()
        // Manejo de la aplicación a la llamada de la API
        ciudadRespuestaCall?.enqueue(object : Callback<CiudadRespuesta?> {
            override fun onResponse(
                call: Call<CiudadRespuesta?>,
                response: Response<CiudadRespuesta?>
            ) {
                if (response.isSuccessful) {
                    // Obtener información de la API
                    val ciudadRespuesta = response.body()
                    val listaCiudad: ArrayList<Ciudad>? = ciudadRespuesta?.links?.cities

                    // Obtener los nombres de las ciudades
                    val nombresCiudades = listaCiudad?.map { it.name } ?: emptyList()

                    // Configurar el Spinner con los nombres de las ciudades
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        nombresCiudades
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    citySpinner.adapter = adapter

                    // Prueba de depuración en log con filtro "CIUDADES"
                    for (ciudad in listaCiudad!!) {
                        Log.d(TAG, "Ciudad: ${ciudad.name}")
                    }

                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody())
                }
            }

            override fun onFailure(call: Call<CiudadRespuesta?>, t: Throwable) {
                Log.e(TAG, "- Error: ", t)
            }
        })
    }


}