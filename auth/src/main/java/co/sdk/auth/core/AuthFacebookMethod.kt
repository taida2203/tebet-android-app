package co.sdk.auth.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import java.util.Arrays

import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginConfiguration
import co.sdk.auth.core.models.LoginException

class AuthFacebookMethod : AuthMethod {
    private var callbackManager: CallbackManager? = null
    val isLoggedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null
        }

    val id: String?
        get() = if (isLoggedIn) {
            AccessToken.getCurrentAccessToken().userId
        } else null

    interface FacebookEmailCallback {
        fun onGetEmail(s: String?, firstName: String?, email: String?)

        fun onFailed()
    }

    override fun brandedLogin(context: Activity, configuration: LoginConfiguration, callback: ApiCallBack<LoginConfiguration>) {
        callbackManager = CallbackManager.Factory.create()
        if (!isLoggedIn) {
            LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    configuration.token = loginResult.accessToken.token
                    callback.onSuccess(200, configuration)
                }

                override fun onCancel() {
                    callback.onFailed(LoginException(502, "Cancelled"))
                }

                override fun onError(error: FacebookException) {
                    callback.onFailed(LoginException(502, error.message))
                }
            })
            LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile", "email"))
        } else {
            // already login
            configuration.token = AccessToken.getCurrentAccessToken().token
            callback.onSuccess(200, configuration)
        }
    }

    fun getEmail(callback: FacebookEmailCallback?) {
        if (!isLoggedIn && callback != null) {
            callback.onFailed()
            return
        }
        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            var email: String? = null
            try {
                email = `object`.getString("email")
            } catch (ignored: Exception) {
            }

            if (callback != null) {
                var firstName: String? = null
                var lastName: String? = null
                try {
                    firstName = `object`.getString("first_name")
                    lastName = `object`.getString("last_name")
                } catch (ignored: Exception) {
                }

                callback.onGetEmail(email, firstName, lastName)
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "email,first_name,last_name")
        request.parameters = parameters
        request.executeAsync()

    }

    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
        if (AccessToken.getCurrentAccessToken() == null) {
            callback?.onSuccess(200,null)
            return
        }
        val profile = Profile.getCurrentProfile()
        if (profile != null) {
            GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
        } else {
            LoginManager.getInstance().logOut()
        }

        callback?.onSuccess(200, null )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (callbackManager != null) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

}
