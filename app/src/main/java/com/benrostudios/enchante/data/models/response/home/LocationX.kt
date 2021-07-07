package com.benrostudios.enchante.data.models.response.home

data class LocationX(
    val coordinates: Coordinates = Coordinates(),
    val humanformAddress: String? = "",
    val mapsUrl: String? = ""
)