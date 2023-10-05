package com.example.lab6_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.bumptech.glide.Glide
import com.example.lab6_api.CityApi.CiudadApiService
import com.example.lab6_api.Elementos.Ciudad
import com.example.lab6_api.Elementos.CiudadRespuesta
import com.example.lab6_api.Elementos.ImagenRespuesta
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

                    // Establecer un listener para el spinner para obtener la imagen cuando se selecciona una ciudad
                    citySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                val ciudadSeleccionada = listaCiudad?.get(position)
                                // Obtener el nombre de la ciudad para obtener la imagen de acuerdo al nombre
                                val slugName =
                                    ciudadSeleccionada?.href?.split("slug:")?.get(1)?.split("/")
                                        ?.get(0)
                                if (slugName != null) {
                                    obtenerImagenDeCiudad(slugName)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {}
                        }

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

    fun obtenerImagenDeCiudad(slugName: String) {
        // Instancia de CiudadApiService para el uso de retrofit para realizar petición de API
        val service: CiudadApiService = retrofit!!.create(CiudadApiService::class.java)
        // Manejo de la aplicación a la llamada de la API
        service.obtenerImagenCiudad(slugName).enqueue(object : Callback<ImagenRespuesta> {
            override fun onResponse(
                call: Call<ImagenRespuesta>,
                response: Response<ImagenRespuesta>
            ) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.photos?.get(0)?.image?.mobile
                    Log.d(TAG, "Imagen de $slugName: $imageUrl")

                    // Cargar la imagen en el ImageView con Glide
                    Glide.with(this@MainActivity)
                        .load(imageUrl)
                        .into(findViewById(R.id.cityImageView))
                } else {
                    Log.e(
                        TAG,
                        "Error obteniendo imagen para $slugName: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<ImagenRespuesta>, t: Throwable) {
                Log.e(TAG, "Error al obtener imagen para $slugName", t)
            }
        })
    }


}