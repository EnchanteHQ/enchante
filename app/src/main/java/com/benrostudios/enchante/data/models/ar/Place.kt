package com.benrostudios.enchante.data.models.ar

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.ar.sceneform.math.Vector3
import com.google.maps.android.ktx.utils.sphericalDistance
import com.google.maps.android.ktx.utils.sphericalHeading
import kotlin.math.cos
import kotlin.math.sin

data class Place(
    val id: String,
    val icon: String,
    val name: String,
    val geoLocation: GeometryLocation
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Place) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}

fun Place.getPositionVector(azimuth: Float, latLng: LatLng): Vector3 {
    val placeLatLng = this.geoLocation.latLng
    val heading = latLng.sphericalHeading(placeLatLng)
    val distance = latLng.sphericalDistance(placeLatLng)
    val x = distance.toFloat() * sin(azimuth).toFloat()
    val y = 1f
    val z = distance.toFloat() * cos(azimuth).toFloat()
    Log.d("ar_", "${distance.toFloat()}")
    return Vector3(x, y, z)
}

data class GeometryLocation(
    val lat: Double,
    val lng: Double
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}