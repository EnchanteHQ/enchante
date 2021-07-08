package com.benrostudios.enchante.data.models.response.events

data class AssetsX(
    val banner: String? = "",
    val logo: String? = "",
    val otherImages: List<String>? = listOf()
)