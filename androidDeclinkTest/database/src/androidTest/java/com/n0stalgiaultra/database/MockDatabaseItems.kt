package com.n0stalgiaultra.database

import com.n0stalgiaultra.database.entities.PhotoEntity
import com.n0stalgiaultra.domain.model.PhotoModel

val mockDBItem = PhotoEntity(
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


val listDBItems = listOf<PhotoEntity>(
    PhotoEntity(
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
    ),
    PhotoEntity(
        ID_CAPTURA = 2,
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
    ),
    PhotoEntity(
        ID_CAPTURA = 3,
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
)