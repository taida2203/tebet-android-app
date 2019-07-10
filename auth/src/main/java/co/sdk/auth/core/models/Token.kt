package co.sdk.auth.core.models

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token(
        @SerializedName("access_token")
        @Expose
        var accessToken: String?,
        @SerializedName("token_type")
        @Expose
        var tokenType: String?,
        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String?,
        @SerializedName("expires_in")
        @Expose
        var expiresIn: Int,
        @SerializedName("scope")
        @Expose
        var scope: String?,
        var isNewUser: Boolean) {

    val appToken: String
        get() {
            var appToken = ""
            if (!TextUtils.isEmpty(tokenType)) {
                appToken += tokenType
            }
            if (!TextUtils.isEmpty(accessToken)) {
                if (!TextUtils.isEmpty(appToken)) appToken += " "
                appToken += accessToken
            }
            return appToken
        }
}
