package com.n0stalgiaultra.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PhotoAPI {
    @POST("envio")
    fun sendData(@Body data: String): Call<Void>

    companion object{
        const val BASE_URL = "https://www.declink.com/sua-rota-aqui/"
    }
}