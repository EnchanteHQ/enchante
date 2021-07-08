package com.benrostudios.enchante.data.models.response.events

data class Location(
    val coordinates: Coordinates? = Coordinates(),
    val humanformAddress: String? = "",
    val mapsUrl: String? = ""
)