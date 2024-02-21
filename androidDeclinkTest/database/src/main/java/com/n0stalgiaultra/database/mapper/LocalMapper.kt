package com.n0stalgiaultra.database.mapper

import com.n0stalgiaultra.database.entities.PhotoEntity
import com.n0stalgiaultra.domain.model.PhotoModel


fun PhotoEntity.toModel(): PhotoModel{
    return PhotoModel(
        ID_CAPTURA,
        DATA_HORA,
        EQUIPAMENTO,
        CAMERA,
        LATITUDE,
        LONGITUDE,
        IMAGEM,
        MODELO,
        FABRICANTE,
        VERSAO,
        NIVEL_API,
        TIPO_CONEXAO,
        IP_EQUIPAMENTO,
        TRANSMITIDO,
        PERCENTUAL_BATERIA
    )
}

fun PhotoModel.toEntity() : PhotoEntity{
    return PhotoEntity(
        ID_CAPTURA,
        DATA_HORA,
        EQUIPAMENTO,
        CAMERA,
        LATITUDE,
        LONGITUDE,
        IMAGEM,
        MODELO,
        FABRICANTE,
        VERSAO,
        NIVEL_API,
        TIPO_CONEXAO,
        IP_EQUIPAMENTO,
        TRANSMITIDO,
        PERCENTUAL_BATERIA
    )
}