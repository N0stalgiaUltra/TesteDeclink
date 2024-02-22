package com.n0stalgiaultra.domain.model


data class PhotoModel(
    var ID_CAPTURA : Int = 0,
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
){
    fun equals(other: PhotoModel?): Boolean {
        return this.CAMERA == other?.CAMERA &&
                this.DATA_HORA == other?.DATA_HORA &&
                this.LATITUDE == other?.LATITUDE && this.LONGITUDE == other?.LONGITUDE
    }
}
