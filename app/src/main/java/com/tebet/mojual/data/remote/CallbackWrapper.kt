package com.tebet.mojual.data.remote

import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.LoginException
import com.tebet.mojual.data.models.NetworkError
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class CallbackWrapper<T> : DisposableObserver<AuthJson<T>>() {
    protected abstract fun onSuccess(dataResponse: T)
    protected abstract fun onFailure(error: NetworkError)

    final override fun onNext(t: AuthJson<T>) {
        t.data?.let { onSuccess(it) }
    }

    final override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                val responseBody: ResponseBody? = e.response()?.errorBody()
                onFailure(NetworkError(errorCode = e.code(), message = responseBody?.let { getErrorMessage(it) } ?: ""))
            }
            is SocketTimeoutException -> onFailure(NetworkError(message = "Time out"))
            is IOException -> onFailure(NetworkError(message = "Network error"))
            is LoginException -> onFailure(NetworkError(e.errorCode, e.errorMessage))
            else -> onFailure(NetworkError(message = e.message))
        }
    }

    override fun onComplete() {

    }

    private fun getErrorMessage(responseBody: ResponseBody): String? {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
}
