package com.tebet.mojual.data.remote

import co.sdk.auth.core.models.AuthJson
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class CallbackWrapper<T> : DisposableObserver<AuthJson<T>>() {
    protected abstract fun onSuccess(dataResponse: T)
    protected abstract fun onFailure(error: String?)

    final override fun onNext(t: AuthJson<T>) {
        t.data?.let { onSuccess(it) }
    }

    final override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                val responseBody: ResponseBody? = e.response().errorBody()
                onFailure(responseBody?.let { getErrorMessage(it) } ?: "")
            }
            is SocketTimeoutException -> onFailure("Time out")
            is IOException -> onFailure("Network error")
            else -> onFailure(e.message)
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
