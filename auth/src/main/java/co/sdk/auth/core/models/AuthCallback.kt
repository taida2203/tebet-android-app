package co.sdk.auth.core.models

import co.sdk.auth.AuthSdk
import co.sdk.auth.utils.Utility
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
        } else if (response.code() == 400) {
            val exception = AuthException()
            exception.errorCode = response.code()
            try {
                val errorBody = response.errorBody()!!.string()

                val jsonObject = JSONObject(errorBody)
                val userMessage = jsonObject.getJSONArray("messages")
                exception.errorMessage = userMessage.getString(0)
                exception.errorMessages = listOf(userMessage.getString(0))
            } catch (e: Exception) {
                exception.errorMessage = Utility.instance.getString(R.string.general_message_error)
                Timber.d(e)
            }

            onFail(call, exception)
        } else {
            val exeption = AuthException()
            exeption.errorCode = response.code()
            try {
                exeption.errorMessages = response.body()?.getMessage()
            } catch (e: Exception) {
            }

            try {
                exeption.errorMessage = response.message()
            } catch (e: Exception) {
                exeption.errorMessage = Utility.instance.getString(R.string.general_message_error)
            }

            onFail(call, exeption)
        }
    }

    override fun onFailure(call: Call<AuthJson<T>>, t: Throwable) {
        onFail(call, AuthException(0, Utility.instance.getString(R.string.general_message_error)))
    }
}
