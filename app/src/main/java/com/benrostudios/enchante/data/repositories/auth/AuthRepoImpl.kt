package com.benrostudios.enchante.data.repositories.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.enchante.data.models.requests.AuthRequest
import com.benrostudios.enchante.data.network.AuthService
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.data.repositories.BaseNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepoImpl(private val authService: AuthService) : AuthRepo, BaseNetworkRepository() {

    val _authResponse = MutableLiveData<GenericResponse>()

    override suspend fun login(token: String): LiveData<GenericResponse> {
        return withContext(Dispatchers.IO) {
            _authResponse.postValue(
                safeApiCall(
                    call = { authService.authenticateUser(AuthRequest(token)) },
                    error = "Unable to register"
                )!!
            )
            return@withContext _authResponse
        }
    }
}