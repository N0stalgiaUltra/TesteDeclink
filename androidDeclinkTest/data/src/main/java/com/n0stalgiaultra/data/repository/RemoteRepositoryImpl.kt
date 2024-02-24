package com.n0stalgiaultra.data.repository

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalDataSource
import com.n0stalgiaultra.domain.repository.RemoteDataSource
import com.n0stalgiaultra.domain.repository.RemoteRepository

class RemoteRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RemoteRepository {
    override suspend fun sendDataToRemote(photoData: PhotoModel) {
        val result = remoteDataSource.sendRemoteData(photoData)
        if(result.isSuccess)
            localDataSource.transmittedItem(photoData.ID_CAPTURA)

    }
}