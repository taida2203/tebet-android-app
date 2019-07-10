package co.sdk.auth.core.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import co.sdk.auth.AuthSdk

class LogoutInput {
    var token: String? = null
    @SerializedName("device_id")
    @Expose
    var deviceId: String?= null

    init {
        if (AuthSdk.instance.currentToken != null) {
            token = AuthSdk.instance.currentToken!!.accessToken
        }
    }
}
