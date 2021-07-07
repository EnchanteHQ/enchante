package com.benrostudios.enchante.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.data.repositories.auth.AuthRepo
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo): ViewModel() {

    private val _response = MutableLiveData<GenericResponse>()
    val response: LiveData<GenericResponse> = _response

     fun login(token: String) {
        viewModelScope.launch {
            authRepo.login(token)
            _response.value = authRepo.authResponse.value
        }
    }
}