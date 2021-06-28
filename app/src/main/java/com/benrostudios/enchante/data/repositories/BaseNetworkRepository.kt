package com.benrostudios.enchante.data.repositories

import android.util.Log
import com.benrostudios.enchante.data.network.reponse.NetworkEventFailure
import com.benrostudios.enchante.data.network.reponse.NetworkResult
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseNetworkRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
        val result = apiOutput(call, error)
        var output: T? = null
        when (result) {
            is NetworkResult.Success ->
                output = result.output
            is NetworkResult.Error -> {
                EventBus.getDefault().post(NetworkEventFailure(result.exception))
            }
        }
        return output
    }

    private suspend fun <T : Any> apiOutput(
        call: suspend () -> Response<T>,
        error: String
    ): NetworkResult<T> {
        try {
            val response = call()
            Log.d(TAG, "$response")
            return if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(
                    "Server Down"
                )
            }
        } catch (e: UnknownHostException) {
            Log.e(TAG, "$e")
            return NetworkResult.Error(
                "Unresolved Host"
            )
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "$e")
            return NetworkResult.Error(
                "Socket Timeout"
            )
        } catch (e: Exception) {
            Log.e(TAG, "$e")
            return NetworkResult.Error(
                "Something went wrong due to $e"
            )
        }
    }

    companion object {
        const val TAG = "base_network_repository"
    }
}