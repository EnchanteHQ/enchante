package com.benrostudios.enchante.data.repositories.api

import androidx.lifecycle.LiveData
import com.benrostudios.enchante.data.models.requests.RegistrationRequest
import com.benrostudios.enchante.data.network.reponse.GenericResponse

interface ApiRepo {
    suspend fun getHomeRoute(latitude: Double, longitude: Double): LiveData<GenericResponse>
    val homeResponse: LiveData<GenericResponse>
    suspend fun register(registrationRequest: RegistrationRequest): LiveData<GenericResponse>
    val registrationResponse: LiveData<GenericResponse>
}