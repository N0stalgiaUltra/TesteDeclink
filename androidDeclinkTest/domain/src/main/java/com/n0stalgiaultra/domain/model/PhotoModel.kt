package com.n0stalgiaultra.domain.model


data class PhotoModel(
    val ID_CAPTURA : Int = 0,
    val DATA_HORA: String,
    val EQUIPAMENTO : String,
    val CAMERA : String,
    val LATITUDE: Double,
    val LONGITUDE: Double,
    val IMAGEM: String,
    val MODELO: String,
    val FABRICANTE: String,
    val VERSAO : String,
    val NIVEL_API: Int,
    val TIPO_CONEXAO: String,
    val IP_EQUIPAMENTO: String,
    val TRANSMITIDO: Int,
    val PERCENTUAL_BATERIA: Int
)
