package co.sdk.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import co.common.util.PreferenceUtils.*
import co.sdk.auth.core.*
import co.sdk.auth.core.models.*
import co.sdk.auth.network.ServiceHelper
import co.sdk.auth.network.api.ApiService
import co.sdk.auth.network.api.OTPApi
import co.sdk.auth.utils.Utility
import com.facebook.FacebookSdk
import com.tebet.mojual.sdk.auth.BuildConfig
import com.tebet.mojual.sdk.auth.R
import io.reactivex.Observable
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
        init(context)
        Utility.init(context)
        FacebookSdk.setApplicationId(context.resources.getString(R.string.facebook_app_id))
        FacebookSdk.setAutoLogAppEventsEnabled(false)
        FacebookSdk.setAdvertiserIDCollectionEnabled(false)
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
        return authBaseUrl
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

    fun login(context: Activity, authMethod: AuthMethod?, config: LoginConfiguration, isForceLogin: Boolean = true, callback: ApiCallBack<Token>?) {
        if (authMethod !is AuthClientCredentialMethod) {
            this.authMethod = authMethod
            saveObject(AUTH_LOGIN_METHOD, authMethod)
        }
        authMethod?.brandedLogin(context, config, object : ApiCallBack<LoginConfiguration>() {
            override fun onSuccess(code: Int, response: LoginConfiguration?) {
                saveObject(AUTH_BRAND_LOGIN_TOKEN, response)
                if (response != null) {
                    val input = LoginInput()
                    when (authMethod) {
                        is AuthGooglePhoneLoginMethod -> {
                            input.grantType = "firebase"
                            input.token = response.token
                        }
                        is AuthFacebookMethod -> {
                            input.grantType = "facebook_token"
                            input.token = response.token
                            input.token = config.token
                        }
                        is AuthOTPMethod -> {
                            input.grantType = "otp"
                            input.otp = response.otp
                        }
                        is AuthPasswordMethod -> {
                            input.grantType = "password"
                            input.password = response.password
                            input.username = response.username
                        }
                        is AuthGoogleMethod -> {
                            input.grantType = "google_token"
                            input.token = response.token
                        }
                        is AuthAccountKitMethod -> {
                            input.grantType = "account_kit"
                            input.token = response.token
                            input.phone = response.phone
                        }
                        is AuthClientCredentialMethod -> input.grantType = "client_credentials"
                    }

                    if (!TextUtils.isEmpty(input.grantType)) {
                        if (isForceLogin) {
                            retryValidation(if(isForceLogin) 0 else MAX_RETRY_LOGIN, input) {
                                ServiceHelper.createService(ApiService::class.java).login(input).enqueue(object : AuthCallback<Token>() {

                                    override fun onSuccess(code: Int, response: Token?) {
                                        saveObject(AUTH_LOGIN_TOKEN, response)
                                        if (authMethod is AuthAccountKitMethod && config.logoutWhileExpired) {
                                            authMethod.logout(context, true, null)
                                        }
                                        response?.let { callback?.onSuccess(code, it) }
                                    }

                                    override fun onFail(call: Call<AuthJson<Token>>, e: AuthException) {
                                        callback?.onFailed(LoginException(e))
                                    }
                                })
                            }
                        } else {
                            ServiceHelper.createService(ApiService::class.java).login(input).enqueue(object : AuthCallback<Token>() {

                                override fun onSuccess(code: Int, response: Token?) {
                                    saveObject(AUTH_LOGIN_TOKEN, response)
                                    if (authMethod is AuthAccountKitMethod && config.logoutWhileExpired) {
                                        authMethod.logout(context, true, null)
                                    }
                                    response?.let { callback?.onSuccess(code, it) }
                                }

                                override fun onFail(call: Call<AuthJson<Token>>, e: AuthException) {
                                    callback?.onFailed(LoginException(e))
                                }
                            })
                        }
                    } else {
                        callback?.onFailed(LoginException(-1, "Auth method not initial correctly"))
                    }
                } else {
                    callback?.onFailed(LoginException(-1, context.getString(R.string.general_message_error)))
                }
            }

            override fun onFailed(exeption: LoginException) {
                callback?.onFailed(exeption)
            }
        }) ?: callback?.onFailed(LoginException(-1, "Auth method not initial correctly"))
    }

    fun setBaseUrl(baseUrl: String): AuthSdk {
        this.authBaseUrl = baseUrl
        return this
    }

    fun retryValidation(count: Int, input: LoginInput, function: () -> Unit) {
        if (count > MAX_RETRY_LOGIN) {
            function()
            return
        }
        Timber.e("DOLPHIN try " + count)
        Handler().postDelayed({
            ServiceHelper.createService(ApiService::class.java).login(input)
                .enqueue(object : AuthCallback<Token>() {

                    override fun onSuccess(code: Int, response: Token?) {
                        function()
                    }

                    override fun onFail(call: Call<AuthJson<Token>>, e: AuthException) {
                        retryValidation(count + 1, input, function)
                    }
                })
        }, 3000)

    }

    fun getAuthMethod(): AuthMethod? {
        return authMethod
    }

    fun getBrandLoginToken(): LoginConfiguration? {
        return getObject<LoginConfiguration>(AUTH_BRAND_LOGIN_TOKEN, LoginConfiguration::class.java) as LoginConfiguration
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (getAuthMethod() is AuthGooglePhoneLoginMethod) {
            (getAuthMethod() as AuthGooglePhoneLoginMethod).onActivityResult(requestCode, resultCode, data)
        }
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

    fun login(context: Activity, authMethod: AuthMethod?, config: LoginConfiguration, isForceLogin: Boolean = false): Observable<Token> {
        return Observable.create { emitter ->
            run {
                login(context, authMethod, config, isForceLogin, object : ApiCallBack<Token>() {
                    override fun onSuccess(code: Int, response: Token?) {
                        response?.let { emitter.onNext(it) } ?: emitter.onNext(Token())
                    }

                    override fun onFailed(exeption: LoginException) {
                        emitter.onError(Throwable(exeption.errorMessage))
                    }
                })
            }

        }
    }

    fun logout(forceLogout: Boolean = false): Observable<Any> {
        return Observable.create { emitter ->
            run {
                logout(forceLogout, object : ApiCallBack<Any>() {
                    override fun onSuccess(code: Int, response: Any?) {
                        emitter.onNext("")
                    }

                    override fun onFailed(exeption: LoginException) {
                        emitter.onError(Throwable(exeption.errorMessage))
                    }
                })
            }

        }
    }

    fun logout(forceLogout: Boolean, callback: ApiCallBack<Any>?) {
        if (getAuthMethod() == null) {
            saveObject(AUTH_LOGIN_TOKEN, null)
            clearAll()
            callback?.onSuccess(200, AuthJson<Any>("true", context.getString(R.string.general_message_error)))
            return
        }
        getAuthMethod()?.logout(context, forceLogout, object : ApiCallBack<Any>() {
            override fun onSuccess(code: Int, response: Any?) {
                val logoutInput = LogoutInput()
                logoutInput.deviceId = deviceId
                ServiceHelper.createService(ApiService::class.java).logout(logoutInput).enqueue(object : Callback<AuthJson<Any>> {
                    override fun onResponse(call: Call<AuthJson<Any>>, response: Response<AuthJson<Any>>) {
                        saveObject(AUTH_LOGIN_TOKEN, null)
                        clearAll()
//                        response.body()?.let {
//                            callback?.onSuccess(code, it)
//                        }
                        callback?.onSuccess(code,response.body())
                        return
                    }

                    override fun onFailure(call: Call<AuthJson<Any>>, t: Throwable) {
                        if (forceLogout) {
                            saveObject(AUTH_LOGIN_TOKEN, null)
                            clearAll()
                            if (callback != null) {
                                val errorMessage = t.message
                                callback.onSuccess(200, AuthJson<Any>("true", errorMessage ?: ""))
                            }
                        } else {
                            callback?.onFailed(LoginException(-1, context.getString(R.string.general_message_error)))
                        }
                    }
                })
            }

            override fun onFailed(exeption: LoginException) {
                callback?.onFailed(exeption)
            }
        })
    }

    companion object {
        private var sInstance: AuthSdk? = null

        val AUTH_LOGIN_TOKEN = "AUTH_LOGIN_TOKEN"
        val AUTH_LOGIN_METHOD = "AUTH_LOGIN_METHOD"
        val AUTH_BRAND_LOGIN_TOKEN = "AUTH_BRAND_LOGIN_TOKEN"
        const val MAX_RETRY_LOGIN = 15

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
