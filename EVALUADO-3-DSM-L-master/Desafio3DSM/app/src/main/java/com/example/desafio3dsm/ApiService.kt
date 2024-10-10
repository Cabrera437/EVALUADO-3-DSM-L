package com.example.desafio3dsm

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("Lista_de_Recursos_de_Aprendisaje")
    fun obtenerCursos(): Call<List<Curso>>

    @GET("Lista_de_Recursos_de_Aprendisaje/{id}")
    fun obtenerCursoPorId(@Path("id") id: String): Call<Curso>

    @POST("Lista_de_Recursos_de_Aprendisaje")
    fun crearCurso(@Body curso: Curso): Call<Curso>

    @PUT("Lista_de_Recursos_de_Aprendisaje/{id}")
    fun actualizarCurso(@Path("id") id: String, @Body curso: Curso): Call<Curso>

    @DELETE("Lista_de_Recursos_de_Aprendisaje/{id}")
    fun eliminarCurso(@Path("id") id: String): Call<Void>

    companion object {
        private const val BASE_URL = "https://67061ac1031fd46a8311f3da.mockapi.io/api/v2/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
