package com.n0stalgiaultra.domain.repository

interface RemoteRepository {

    suspend fun sendDataToRemote()
}