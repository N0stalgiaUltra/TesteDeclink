package com.n0stalgiaultra.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.n0stalgiaultra.database.dao.PhotoEntityDAO
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: PhotoEntityDAO
    private lateinit var db: AppDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        dao = db.getDao()
    }

    @After
    fun dbClose(){
        db.close()

    }

    @Test
    fun should_insert_item_to_db() = runBlocking{
        dao.insertData(mockDBItem)
        val dataList = dao.getAllData()

        assert(dataList.contains(mockDBItem))

    }

    @Test
    fun should_update_TRANSMITIDO_data() = runBlocking {
        dao.insertData(mockDBItem)

        val insertedItem = dao.getData(mockDBItem.ID_CAPTURA)
        assertNotNull(insertedItem)


        dao.transmittedItem(mockDBItem.ID_CAPTURA)

        val updatedItem = dao.getData(mockDBItem.ID_CAPTURA)
        assertNotNull(updatedItem)
        assertEquals(1, updatedItem.TRANSMITIDO)
    }
}