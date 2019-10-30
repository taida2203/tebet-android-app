package co.sdk.auth.core

import android.app.Activity
import android.content.Context

import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginConfiguration

interface AuthMethod {
    fun brandedLogin(context: Activity, configuration: LoginConfiguration,
                     callback: ApiCallBack<LoginConfiguration>)

    fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?)
}
