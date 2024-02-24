package com.n0stalgiaultra.data

import com.google.gson.Gson
import com.n0stalgiaultra.domain.model.PhotoModel
import io.mockk.mockk
import io.mockk.mockkClass
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

val photoModelMock = PhotoModel(
    ID_CAPTURA = 1,
    "03/02/1998",
    "secureId",
    "Frontal",
    1.1,
    0.1,
    "Imagem em Base64",
    "iMock",
    "Apple Mock",
    "11",
    28,
    "Wi-fi",
    "111.111.111.11",
    0,
    100
)
val jsonPhotoModel = Gson().toJson(photoModelMock)
