package com.benrostudios.enchante.data.models.response.events

data class EventIdX(
    val __v: Int? = 0,
    val _id: String? = "",
    val assets: Assets = Assets(),
    val description: String? = "",
    val duration: Duration = Duration(),
    val faq: List<Any>? = listOf(),
    val location: Location = Location(),
    val name: String? = "",
    val parentOrg: String? = "",
    val passes: List<PasseX>? = listOf(),
    val schedule: String? = "",
    val status: String? = "",
    val tags: List<String>? = listOf()
)