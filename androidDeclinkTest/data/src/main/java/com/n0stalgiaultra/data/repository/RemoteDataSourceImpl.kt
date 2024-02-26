package com.n0stalgiaultra.data.repository

import com.google.gson.Gson
import com.n0stalgiaultra.data.api.ApiHandler
import com.n0stalgiaultra.data.api.PhotoAPI
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.RemoteDataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteDataSourceImpl(
    private val api: PhotoAPI
    ): RemoteDataSource {
    private val apiHandler: ApiHandler = ApiHandler()
    override suspend fun sendRemoteData(photoData: PhotoModel): Result<Unit> {
        val dataJson = Gson().toJson(photoData)
        println("Dados JSON: $dataJson")

        val sendData = api.sendData(dataJson)
        return suspendCoroutine {
            continuation ->
            apiHandler.handleApiCall(sendData,
                onSuccess = {
                    response ->
                    continuation.resume(Result.success(Unit))
                },
                onFailure = {
                    throwable ->
                    continuation.resume(Result.failure(throwable))
                })
        }

    }
}