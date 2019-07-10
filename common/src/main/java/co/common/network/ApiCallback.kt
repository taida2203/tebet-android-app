package co.common.network

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<co.common.network.JsonWrapper<T>> {

    abstract fun onSuccess(response: T?)

    abstract fun onFail(call: Call<*>?, e: co.common.network.ApiException)

    final override fun onResponse(call: Call<co.common.network.JsonWrapper<T>>, response: Response<co.common.network.JsonWrapper<T>>) {
        if (response.isSuccessful) {
            onSuccess(response.body()?.data)
        } else {
            val exception = co.common.network.ApiException()
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

    final override fun onFailure(call: Call<co.common.network.JsonWrapper<T>>?, t: Throwable?) {
        onFail(call, co.common.network.ApiException(t))
    }
}
