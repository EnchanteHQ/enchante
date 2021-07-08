package com.benrostudios.enchante.data.models.response.events

data class LocationX(
    val coordinates: Coordinates = Coordinates(),
    val humanformAddress: String? = "",
    val mapsUrl: String? = ""
)