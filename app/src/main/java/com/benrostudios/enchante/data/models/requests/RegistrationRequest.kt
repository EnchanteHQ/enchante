package com.benrostudios.enchante.data.models.requests

data class RegistrationRequest(
    val description: String? = "",
    val imgUrl: String? = "",
    val interests: List<String>? = listOf(),
    val phoneNumber: String? = "",
    val socialLink: String? = ""
)