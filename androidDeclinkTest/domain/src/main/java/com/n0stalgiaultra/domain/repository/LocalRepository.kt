package com.n0stalgiaultra.domain.repository

import com.n0stalgiaultra.domain.model.PhotoModel

interface LocalRepository {

    suspend fun getPhotoData(id: Int): PhotoModel

    suspend fun getAllPhotoData(): List<PhotoModel>

    suspend fun insertPhotoData(item: PhotoModel)
}