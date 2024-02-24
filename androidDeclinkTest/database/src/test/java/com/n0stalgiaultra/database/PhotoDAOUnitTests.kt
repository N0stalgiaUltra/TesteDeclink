package com.n0stalgiaultra.database

import com.n0stalgiaultra.database.dao.PhotoEntityDAO
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test


class PhotoDAOUnitTests {

    private val dao: PhotoEntityDAO = mockk()

    @Test
    fun `Should insert item to database`() = runBlocking{
        dao.insertData(com.n0stalgiaultra.database.mockDBItem)
        every { dao.insertData(com.n0stalgiaultra.database.mockDBItem) } returns Unit

        val expected = dao.getAllData()
        every {dao.getAllData() } returns com.n0stalgiaultra.database.listDBItems

        assertTrue(expected.contains(com.n0stalgiaultra.database.mockDBItem))
    }

    @Test
    fun `Should return all PhotoEntity data`(){

    }
    @Test
    fun `Should Update TRANSMITTED item field`(){

    }
}