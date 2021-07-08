package com.benrostudios.enchante.data.network.reponse

data class GenericResponse(
    val success: String,
    val message: String,
    val data: ResponseMetrics
)