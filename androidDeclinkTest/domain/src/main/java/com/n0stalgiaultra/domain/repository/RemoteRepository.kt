package com.n0stalgiaultra.domain.repository

import com.n0stalgiaultra.domain.model.PhotoModel

interface RemoteRepository {
    suspend fun sendDataToRemote(photoData: PhotoModel)
}