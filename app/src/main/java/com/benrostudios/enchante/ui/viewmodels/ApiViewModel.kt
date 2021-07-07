package com.benrostudios.enchante.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.data.repositories.api.ApiRepo
import kotlinx.coroutines.launch

class ApiViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private val _response = MutableLiveData<GenericResponse>()
    val response: LiveData<GenericResponse> = _response

    fun homeRoute(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            apiRepo.getHomeRoute(latitude, longitude)
            _response.value = apiRepo.homeResponse.value
        }
    }
}