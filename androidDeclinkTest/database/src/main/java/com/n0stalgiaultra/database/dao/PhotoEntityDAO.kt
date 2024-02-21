package com.n0stalgiaultra.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.n0stalgiaultra.database.entities.PhotoEntity

@Dao
interface PhotoEntityDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(item: PhotoEntity)

    @Query("SELECT * FROM PhotoEntity")
    fun getAllData() : List<PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE ID_CAPTURA = :id ")
    fun getData(id: Int): PhotoEntity

    @Query("UPDATE PhotoEntity SET TRANSMITIDO = 1 WHERE ID_CAPTURA =:id AND TRANSMITIDO = 0")
    fun transmittedItem(id: Int)
}