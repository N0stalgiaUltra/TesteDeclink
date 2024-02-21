package com.n0stalgiaultra.domain.repository

import com.n0stalgiaultra.domain.model.PhotoModel

interface LocalDataSource {
    suspend fun insertData(item: PhotoModel)
    suspend fun getAllData() : List<PhotoModel>

    suspend fun getData(id: Int): PhotoModel
}