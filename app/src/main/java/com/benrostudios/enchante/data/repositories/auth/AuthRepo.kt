package com.benrostudios.enchante.data.repositories.auth

import androidx.lifecycle.LiveData
import com.benrostudios.enchante.data.network.reponse.GenericResponse

interface AuthRepo {
    suspend fun login(token: String): LiveData<GenericResponse>
}