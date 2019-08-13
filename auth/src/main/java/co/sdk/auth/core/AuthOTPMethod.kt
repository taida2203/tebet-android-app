package co.sdk.auth.core

import android.app.Activity
import android.content.Context

import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginConfiguration

class AuthOTPMethod : AuthMethod {

    override fun brandedLogin(context: Activity, configuration: LoginConfiguration, callback: ApiCallBack<LoginConfiguration>) {
        callback.onSuccess(200, configuration)
    }

    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
        callback?.onSuccess(200, null)
    }
}
