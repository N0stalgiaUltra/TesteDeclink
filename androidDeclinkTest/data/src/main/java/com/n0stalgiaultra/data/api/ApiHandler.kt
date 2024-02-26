package com.n0stalgiaultra.data.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ApiHandler {
    fun <T> handleApiCall(
        call: Call<T>,
        onSuccess: (T?) -> Unit,
        onFailure: (Throwable)-> Unit){
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful)
                    onSuccess(response.body())
                else
                    onFailure(Exception("Falha no envio de dados: ${response.code()}"))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e("ApiHandler", "Erro", t)
            }
        })
    }
}