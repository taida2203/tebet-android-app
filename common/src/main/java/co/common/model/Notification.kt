package co.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Notification {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}
