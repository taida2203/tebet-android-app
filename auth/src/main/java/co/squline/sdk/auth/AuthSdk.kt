package co.squline.sdk.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log

import com.facebook.FacebookSdk
import com.tebet.mojual.common.rtc.util.PreferenceUtils

import co.squline.sdk.auth.core.AuthAccountKitMethod
import co.squline.sdk.auth.core.AuthClientCredentialMethod
import co.squline.sdk.auth.core.AuthFacebookMethod
import co.squline.sdk.auth.core.AuthGoogleMethod
import co.squline.sdk.auth.core.AuthMethod
import co.squline.sdk.auth.core.AuthOTPMethod
import co.squline.sdk.auth.core.AuthPasswordMethod
import co.squline.sdk.auth.core.LoginConfiguration
import co.squline.sdk.auth.core.models.ApiCallBack
import co.squline.sdk.auth.core.models.AuthCallback
import co.squline.sdk.auth.core.models.AuthException
import co.squline.sdk.auth.core.models.AuthJson
import co.squline.sdk.auth.core.models.LoginException
import co.squline.sdk.auth.core.models.LoginInput
import co.squline.sdk.auth.core.models.LogoutInput
import co.squline.sdk.auth.core.models.OTP
import co.squline.sdk.auth.core.models.RequestOTPInput
import co.squline.sdk.auth.core.models.Token
import co.squline.sdk.auth.network.ServiceHelper
import co.squline.sdk.auth.network.api.ApiService
import co.squline.sdk.auth.network.api.OTPApi
import co.squline.sdk.auth.utils.Utility
import com.tebet.mojual.common.rtc.util.PreferenceUtils.*
import com.tebet.mojual.sdk.auth.BuildConfig
import com.tebet.mojual.sdk.auth.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class AuthSdk(val context: Context, var authBaseUrl: String?, val consumerKey: String, val consumerSecret: String, val deviceId: String) {

    internal var authMethod: AuthMethod? = null
    val currentToken: Token?
        get() {
            var currentToken: Token? = null
            try {
                currentToken = getObject<Token>(AUTH_LOGIN_TOKEN, Token::class.java) as Token
            } catch (e: Exception) {
            }

            return currentToken
        }

    init {
        PreferenceUtils.init(context)
        Utility.init(context)
        FacebookSdk.setApplicationId("1670061399880500")
        FacebookSdk.sdkInitialize(context)
        FacebookSdk.setAutoLogAppEventsEnabled(false)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        saveString("device_id", deviceId)
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

    fun getBaseUrl(): String? {
//        return authBaseUrl
//        return "http://private-2087f-taidao.apiary-mock.com"
        return "http://192.168.4.1"
    }

    fun requestOTP(phone: String, callback: AuthCallback<OTP>) {
        requestOTP(phone, "LOGIN", callback)
    }

    fun requestOTP(phone: String, type: String, callback: AuthCallback<OTP>) {
        val input = RequestOTPInput()
        input.phoneNumber = phone
        input.type = type
        input.setPreferEmail(true)

        ServiceHelper.createService(OTPApi::class.java).requestOTP(input).enqueue(callback)
    }

    fun login(context: Activity, authMethod: AuthMethod?, config: LoginConfiguration, callback: ApiCallBack<Token>?) {
        if (authMethod !is AuthClientCredentialMethod) {
            this.authMethod = authMethod
            saveObject(AUTH_LOGIN_METHOD, authMethod)
        }
        authMethod!!.brandedLogin(context, config, object : ApiCallBack<LoginConfiguration>() {
            override fun onSuccess(code: Int, response: LoginConfiguration?) {
                if (response != null) {
                    val input = LoginInput()
                    if (authMethod is AuthFacebookMethod) {
                        input.grant_type = "facebook_token"
                        input.token = response.token
                        input.token = config.token
                    } else if (authMethod is AuthOTPMethod) {
                        input.grant_type = "otp"
                        input.otp = response.otp
                    } else if (authMethod is AuthPasswordMethod) {
                        input.grant_type = "password"
                        input.password = response.password
                        input.email = response.email
                    } else if (authMethod is AuthGoogleMethod) {
                        input.grant_type = "google_token"
                        input.token = response.token
                    } else if (authMethod is AuthAccountKitMethod) {
                        input.grant_type = "accountkit_token"
                        input.token = response.token
                    } else if (authMethod is AuthClientCredentialMethod) {
                        input.grant_type = "client_credentials"
                    }

                    if (!TextUtils.isEmpty(input.grant_type)) {
                        ServiceHelper.createService(ApiService::class.java).login(input).enqueue(object : AuthCallback<Token>() {

                            override fun onSuccess(code: Int, response: Token?) {
                                saveObject(AUTH_LOGIN_TOKEN, response)
                                if (authMethod != null && authMethod is AuthAccountKitMethod) {
                                    authMethod.logout(context, true, null)
                                }
                                response?.let { callback?.onSuccess(code, it) }
                            }

                            override fun onFail(call: Call<AuthJson<Token>>, e: AuthException) {
                                callback?.onFailed(LoginException(e))
                            }
                        })
                    } else {
                        callback!!.onFailed(LoginException(-1, "Auth method not initial correctly"))
                    }
                } else {
                    callback?.onFailed(LoginException(-1, context.getString(R.string.general_message_error)))
                }
            }

            override fun onFailed(exeption: LoginException) {
                callback?.onFailed(exeption)
            }
        })
    }

    fun setBaseUrl(baseUrl: String): AuthSdk {
        this.authBaseUrl = baseUrl
        return this
    }

    fun getAuthMethod(): AuthMethod? {
        if (authMethod == null) {
            authMethod = getObject<AuthFacebookMethod>(AUTH_LOGIN_METHOD, AuthFacebookMethod::class.java) as AuthFacebookMethod
        }
        return authMethod
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (getAuthMethod() is AuthFacebookMethod) {
            (getAuthMethod() as AuthFacebookMethod).onActivityResult(requestCode, resultCode, data)
        }
        if (getAuthMethod() is AuthAccountKitMethod) {
            (authMethod as AuthAccountKitMethod).onActivityResult(requestCode, resultCode, data)
        }
        if (getAuthMethod() is AuthGoogleMethod) {
            (authMethod as AuthGoogleMethod).onActivityResult(requestCode, resultCode, data)
        }
    }

    fun logout(forceLogout: Boolean, callback: ApiCallBack<Any>) {
        if (getAuthMethod() == null) {
            callback.onSuccess(200, AuthJson<Any>("true", context.getString(R.string.general_message_error)))
            return
        }
        getAuthMethod()!!.logout(context, forceLogout, object : ApiCallBack<Any>() {
            override fun onSuccess(code: Int, response: Any?) {
                val logoutInput = LogoutInput()
                logoutInput.deviceId = deviceId
                ServiceHelper.createService(ApiService::class.java).logout(logoutInput).enqueue(object : Callback<AuthJson<Any>> {
                    override fun onResponse(call: Call<AuthJson<Any>>, response: Response<AuthJson<Any>>) {
                        saveObject(AUTH_LOGIN_TOKEN, null)
//                        response.body()?.let {
//                            callback?.onSuccess(code, it)
//                        }
                        callback.onSuccess(code,response.body())
                        return
                    }

                    override fun onFailure(call: Call<AuthJson<Any>>, t: Throwable) {
                        if (forceLogout) {
                            saveObject(AUTH_LOGIN_TOKEN, null)
                            if (callback != null) {
                                val errorMessage = t.message
                                callback.onSuccess(200, AuthJson<Any>("true", errorMessage ?: ""))
                            }
                        } else {
                            callback!!.onFailed(LoginException(-1, context.getString(R.string.general_message_error)))
                        }
                    }
                })
            }

            override fun onFailed(exeption: LoginException) {
                callback!!.onFailed(exeption)
            }
        })
    }

    companion object {
        private var sInstance: AuthSdk? = null

        val AUTH_LOGIN_TOKEN = "AUTH_LOGIN_TOKEN"
        val AUTH_LOGIN_METHOD = "AUTH_LOGIN_METHOD"

        val instance: AuthSdk
            get() {
                if (sInstance == null) {
                    throw IllegalStateException("Uninitialized.")
                }
                return sInstance as AuthSdk
            }

        fun init(context: Context, baseUrl: String, consumerKey: String, consumerSecret: String, deviceId: String) {
            if (sInstance != null) {
            }
            sInstance = AuthSdk(context, baseUrl, consumerKey, consumerSecret, deviceId)
        }
    }
}
