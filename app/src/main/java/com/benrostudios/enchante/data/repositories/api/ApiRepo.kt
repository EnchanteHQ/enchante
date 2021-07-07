package com.benrostudios.enchante.data.repositories.api

import androidx.lifecycle.LiveData
import com.benrostudios.enchante.data.network.reponse.GenericResponse

interface ApiRepo {
    suspend fun getHomeRoute(latitude: Double, longitude: Double): LiveData<GenericResponse>
    val homeResponse: LiveData<GenericResponse>
}