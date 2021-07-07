package com.benrostudios.enchante.data.models.response.home

data class Assets(
    val banner: String? = "",
    val logo: String? = "",
    val otherImages: List<String>? = listOf()
)