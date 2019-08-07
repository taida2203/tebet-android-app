package co.sdk.auth.core.models

import co.sdk.auth.AuthSdk
import co.sdk.auth.utils.Utility
import com.google.gson.JsonObject
import com.tebet.mojual.sdk.auth.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

abstract class AuthCallback<T> : Callback<AuthJson<T>> {

    abstract fun onSuccess(code: Int, response: T?)

    abstract fun onFail(call: Call<AuthJson<T>>, e: AuthException)

    override fun onResponse(call: Call<AuthJson<T>>, response: Response<AuthJson<T>>) {
        if (response.isSuccessful) {
            onSuccess(response.code(), response.body()!!.data)
            /*try {
                response.raw().body().close();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else if (response.code() == 403) {
            /*try {
                response.raw().body().close();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            AuthSdk.instance.logout(true, object : ApiCallBack<Any>() {
                override fun onSuccess(code: Int, o: Any?) {
                    onFail(call, AuthException(401, response.message()))
                }

                override fun onFailed(e: LoginException) {
                    onFail(call, AuthException(401, response.message()))
                }
            })
        } else {
            val exception = AuthException()
            exception.errorCode = response.code()
            val errorBody = response.errorBody()?.string()
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(errorBody)
                exception.errorMessage = jsonObject.getString("message")
            } catch (e: Exception) {
                val userMessages = jsonObject?.getJSONArray("messages")
                try {
                    exception.errorMessages = listOf(userMessages!!.getString(0))
                } catch (e: Exception) {
                    exception.errorMessage = Utility.instance.getString(R.string.general_message_error)
                }
            }

            onFail(call, exception)
        }
//        else {
//            val exeption = AuthException()
//            exeption.errorCode = response.code()
//            try {
//                exeption.errorMessages = response.body()?.getMessage()
//            } catch (e: Exception) {
//            }
//
//            try {
//                exeption.errorMessage = response.message()
//            } catch (e: Exception) {
//                exeption.errorMessage = Utility.instance.getString(R.string.general_message_error)
//            }
//
//            onFail(call, exeption)
//        }
    }

    override fun onFailure(call: Call<AuthJson<T>>, t: Throwable) {
        onFail(call, AuthException(0, Utility.instance.getString(R.string.general_message_error)))
    }
}
