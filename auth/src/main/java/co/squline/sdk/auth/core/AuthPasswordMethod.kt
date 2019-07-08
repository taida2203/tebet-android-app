package co.squline.sdk.auth.core

import android.app.Activity
import android.content.Context

import co.squline.sdk.auth.core.models.ApiCallBack

class AuthPasswordMethod : AuthMethod {

    override fun brandedLogin(context: Activity, configuration: LoginConfiguration, callback: ApiCallBack<LoginConfiguration>) {
        callback.onSuccess(200, configuration)
    }

    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
        callback?.onSuccess(200, null)
    }
}
