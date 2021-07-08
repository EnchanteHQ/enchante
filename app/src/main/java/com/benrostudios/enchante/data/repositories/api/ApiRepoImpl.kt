package com.benrostudios.enchante.data.repositories.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.enchante.data.models.requests.RegistrationRequest
import com.benrostudios.enchante.data.network.ApiService
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.data.repositories.BaseNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepoImpl(private val apiService: ApiService) : BaseNetworkRepository(), ApiRepo {

    private val _homeResponse = MutableLiveData<GenericResponse>()
    private val _registrationResponse = MutableLiveData<GenericResponse>()
    private val _eventsResponse = MutableLiveData<GenericResponse>()

    override suspend fun getHomeRoute(
        latitude: Double,
        longitude: Double
    ): LiveData<GenericResponse> {
        return withContext(Dispatchers.IO) {
            _homeResponse.postValue(
                safeApiCall(
                    call = { apiService.homeRoute(latitude, longitude) },
                    error = "Unable to register"
                )!!
            )
            return@withContext _homeResponse
        }
    }


    override val homeResponse: LiveData<GenericResponse>
        get() = _homeResponse

    override suspend fun register(registrationRequest: RegistrationRequest): LiveData<GenericResponse> {
        return withContext(Dispatchers.IO) {
            _registrationResponse.postValue(
                safeApiCall(
                    call = { apiService.register(registrationRequest) },
                    error = "Unable to register"
                )!!
            )
            return@withContext _registrationResponse
        }
    }

    override val registrationResponse: LiveData<GenericResponse>
        get() = _registrationResponse

    override suspend fun getMyEvents(): LiveData<GenericResponse> {
        return withContext(Dispatchers.IO) {
            _eventsResponse.postValue(
                safeApiCall(
                    call = { apiService.getMyEvents() },
                    error = "Unable to register"
                )!!
            )
            return@withContext _eventsResponse
        }
    }

    override val eventsResponse: LiveData<GenericResponse>
        get() = _eventsResponse
}