package co.squline.sdk.auth.core

import android.app.Activity
import android.content.Context

import co.squline.sdk.auth.core.models.ApiCallBack

interface AuthMethod {
    fun brandedLogin(context: Activity, configuration: LoginConfiguration,
                     callback: ApiCallBack<LoginConfiguration>)

    fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?)
}
