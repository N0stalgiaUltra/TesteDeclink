package com.n0stalgiaultra.database.repository

import android.util.Log
import com.n0stalgiaultra.database.dao.PhotoEntityDAO
import com.n0stalgiaultra.database.mapper.toEntity
import com.n0stalgiaultra.database.mapper.toModel
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.domain.repository.LocalDataSource

class LocalDataSourceImpl(private val dao: PhotoEntityDAO) : LocalDataSource {
    override suspend fun insertData(item: PhotoModel) {
        try {
            dao.insertData(item.toEntity())
        } catch (e: Exception) {
            Log.e("LocalDataSource", "Erro de inserção", e)
        }
    }

    override suspend fun getAllData(): List<PhotoModel> {
        return dao.getAllData().map {
            it.toModel()
        }
    }

    override suspend fun getData(id: Int): PhotoModel {
        return dao.getData(id).toModel()
    }
}