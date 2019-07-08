package com.tebet.mojual.common.rtc.network

import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

abstract class RxSquCallback<T> : DisposableObserver<T>() {

    abstract fun onSuccess(response: T)

    abstract fun onFail(e: SquException)

    override fun onNext(response: T) {
        if (response is BaseResponse && response.isStatus.not()) {
            onFail(SquException(500, response.message ?: ""))
            return
        }

        if (response is SquBaseResponse<*> && response.status?.not() == true) {
            if (response.mobileMessage.isNullOrBlank().not()) {
                onFail(SquException(500, response.mobileMessage
                        ?: ""))
                return
            }

            onFail(SquException(500, response.message ?: ""))
            return
        }

        if (response is Response<*> && !response.isSuccessful) {
            response.errorBody()?.let { handleError(response.code(), it) }
        }

        if (isDisposed.not()) onSuccess(response)
    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {
            e.response().errorBody()?.let { handleError(e.response().code(), it) }
        } else {
            onFail(SquException())
        }
    }

    override fun onComplete() {
    }

    private fun handleError(errorCode: Int, responseBody: ResponseBody) {
        val exception = SquException().apply { this.errorCode = errorCode }
        val errorContent = responseBody.string()
        if (errorContent.isNullOrBlank() || errorContent.isNullOrEmpty() || errorCode >= 500) {
            onFail(exception)
            return
        }

        val jsonObject = JSONObject(errorContent)
        var message: String

        try {
            message = jsonObject.getString("message")
            exception.errorMessage = message

            onFail(exception)
            return
        } catch (ignored: Exception) {
        }

        try {
            val messages = jsonObject.getJSONArray("messages")
            message = messages.getString(0)
            exception.errorMessage = message

            onFail(exception)
            return
        } catch (ignored: Exception) {
        }

        onFail(exception)

    }
}