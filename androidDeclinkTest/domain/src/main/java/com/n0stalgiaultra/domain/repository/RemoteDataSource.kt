package com.n0stalgiaultra.domain.repository

import com.n0stalgiaultra.domain.model.PhotoModel

interface RemoteDataSource {
    suspend fun sendRemoteData(photoData: PhotoModel): Result<Unit>
}