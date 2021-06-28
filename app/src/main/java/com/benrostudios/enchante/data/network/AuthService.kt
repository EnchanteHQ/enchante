package com.benrostudios.enchante.data.network

import android.content.Context
import com.benrostudios.enchante.data.models.requests.AuthRequest
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.utils.Constants
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {


    @POST("auth/google")
    suspend fun authenticateUser(@Body authRequest: AuthRequest): Response<GenericResponse>


    companion object {
        operator fun invoke(
            context: Context,
        ): AuthService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(context))
                .build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(AuthService::class.java)
        }
    }
}