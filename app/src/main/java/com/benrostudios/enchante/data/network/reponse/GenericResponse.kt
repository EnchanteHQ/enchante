package com.benrostudios.enchante.data.network.reponse

data class GenericResponse(
    val status: String,
    val message: String,
    val data: ResponseMetrics
)