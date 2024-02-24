package com.n0stalgiaultra.data.repository

import com.google.gson.Gson
import com.n0stalgiaultra.data.api.ApiHandler
import com.n0stalgiaultra.data.api.PhotoAPI
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.RemoteDataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteDataSourceImpl(
    private val api: PhotoAPI,
    private val apiHandler: ApiHandler): RemoteDataSource {
    override suspend fun sendRemoteData(photoData: PhotoModel): Result<Unit> {
        val dataJson = Gson().toJson(photoData)
        println("Dados JSON: $dataJson")
        println("Enviando dados para a API...")

        val sendData = api.sendData(dataJson)
        println("Dados Enviados para a API")
        return suspendCoroutine {
            continuation ->
            apiHandler.handleApiCall(sendData,
                onSuccess = {
                    response ->
                    println("Resposta da API recebida com sucesso: $response")

                    continuation.resume(Result.success(Unit))
                },
                onFailure = {
                    throwable ->
                    println("Erro ao receber resposta da API: $throwable")

                    continuation.resume(Result.failure(throwable))
                })
        }

    }
}