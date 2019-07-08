package com.tebet.mojual.common.rtc.network

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<JsonWrapper<T>> {

    abstract fun onSuccess(response: T?)

    abstract fun onFail(call: Call<*>?, e: ApiException)

    final override fun onResponse(call: Call<JsonWrapper<T>>, response: Response<JsonWrapper<T>>) {
        if (response.isSuccessful) {
            onSuccess(response.body()?.data)
        } else {
            val exception = ApiException()
            exception.errorCode = response.code()
            if (response.body() != null) {
                exception.errorMessages = response.body()?.messages
            }
            if (exception.errorMessages == null && response.errorBody() != null) {
                try {
                    val errorMessage = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorMessage)
                    val userMessage = jsonObject.getJSONArray("messages")
                    exception.errorMessage = userMessage.getString(0)
                    exception.errorMessages = listOf(userMessage.getString(0))
                } catch (ignored: Exception) {
                }

            }
            if (exception.errorMessage == null) {
                exception.errorMessage = response.message()
            }
            onFail(call, exception)
        }
    }

    final override fun onFailure(call: Call<JsonWrapper<T>>?, t: Throwable?) {
        onFail(call, ApiException(t))
    }
}
