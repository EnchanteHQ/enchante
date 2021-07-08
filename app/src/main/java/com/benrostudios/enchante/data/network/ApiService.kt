package com.benrostudios.enchante.data.network

import android.content.Context
import com.benrostudios.enchante.data.models.requests.AuthRequest
import com.benrostudios.enchante.data.models.requests.RegistrationRequest
import com.benrostudios.enchante.data.network.reponse.GenericResponse
import com.benrostudios.enchante.utils.Constants
import com.benrostudios.enchante.utils.SharedPrefManager
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/user/home")
    suspend fun homeRoute(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<GenericResponse>

    @POST("/v1/user/profile/registration")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<GenericResponse>


    companion object {
        operator fun invoke(
            context: Context,
            sharedPrefManager: SharedPrefManager
        ): ApiService {
            val authKey = sharedPrefManager.jwtStored
            val requestInterceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .header("Authorization", "Bearer $authKey")
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(ChuckInterceptor(context))
                .build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }
    }
}