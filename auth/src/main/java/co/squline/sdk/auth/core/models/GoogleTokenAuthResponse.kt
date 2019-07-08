package co.squline.sdk.auth.core.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GoogleTokenAuthResponse {

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null
    @SerializedName("scope")
    @Expose
    var scope: String? = null
    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
    @SerializedName("id_token")
    @Expose
    var idToken: String? = null

}
