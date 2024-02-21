package com.n0stalgiaultra.database.repository

import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalDataSource
import com.n0stalgiaultra.domain.repository.LocalRepository

class LocalRepositoryImpl(private val localDataSource: LocalDataSource): LocalRepository {
    override suspend fun insertPhotoData(item: PhotoModel) {
        return localDataSource.insertData(item)
    }

    override suspend fun getAllPhotoData(): List<PhotoModel> {
        return localDataSource.getAllData()
    }

    override suspend fun getPhotoData(id: Int): PhotoModel {
        return localDataSource.getData(id)
    }
}