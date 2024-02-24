package com.n0stalgiaultra.data

import android.util.Log // Importe a biblioteca de log
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.n0stalgiaultra.data.api.ApiHandler
import com.n0stalgiaultra.data.api.PhotoAPI
import com.n0stalgiaultra.data.repository.RemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class PhotoApiUnitTest {

    private lateinit var remoteDataSource: RemoteDataSourceImpl
    private val apiHandler: ApiHandler = mockk<ApiHandler>(relaxed = true)
    private lateinit var photoApi: PhotoAPI
    private lateinit var mockWebServer: MockWebServer


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("PhotoApiUnitTest", "Configurando o teste")
        photoApi = retrofit.create(PhotoAPI::class.java)
        remoteDataSource = RemoteDataSourceImpl(photoApi, apiHandler)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should Return code 200 (success) when Sending data to API`() {
        val successResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{}")
        mockWebServer.enqueue(successResponse)

        val result = photoApi.sendData(jsonPhotoModel).execute()

        assertThat(result.code()).isEqualTo(200)
    }

    @Test
    fun `Should return code 400 (failure) when sending data to API`(){
        val errorResponse = MockResponse()
            .setResponseCode(400) // Escolha o código de erro desejado
        mockWebServer.enqueue(errorResponse)

        val result = photoApi.sendData(jsonPhotoModel).execute()
        assertThat(result.code()).isEqualTo(400) // Verifica se o código de erro retornado
    }
}
