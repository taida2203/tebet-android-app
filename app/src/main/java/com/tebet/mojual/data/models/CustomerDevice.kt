package com.tebet.mojual.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerDevice {
    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null
    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
}
