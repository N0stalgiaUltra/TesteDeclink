package com.n0stalgiaultra.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.n0stalgiaultra.database.dao.PhotoEntityDAO
import com.n0stalgiaultra.database.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): PhotoEntityDAO

    companion object{
        const val DATABASE_NAME = "declink-test-app-db"

    }
}