package com.benrostudios.enchante.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benrostudios.enchante.data.models.requests.RegistrationRequest
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.data.repositories.api.ApiRepo
import kotlinx.coroutines.launch

class ApiViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private val _response = MutableLiveData<GenericResponse>()
    val response: LiveData<GenericResponse> = _response

    private val _regResponse = MutableLiveData<GenericResponse>()
    val regResponse: LiveData<GenericResponse> = _regResponse

    private val _eventsResponse = MutableLiveData<GenericResponse>()
    val eventsResponse: LiveData<GenericResponse> = _eventsResponse

    fun homeRoute(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            apiRepo.getHomeRoute(latitude, longitude)
            _response.value = apiRepo.homeResponse.value
        }
    }

    fun registerRoute(registrationRequest: RegistrationRequest) {
        viewModelScope.launch {
            apiRepo.register(registrationRequest)
            _regResponse.value = apiRepo.registrationResponse.value
        }
    }

    fun getMyEventsRoute() {
        viewModelScope.launch {
            apiRepo.getMyEvents()
            _eventsResponse.value = apiRepo.eventsResponse.value
        }
    }
}