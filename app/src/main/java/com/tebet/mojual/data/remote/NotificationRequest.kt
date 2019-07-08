package com.tebet.mojual.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationRequest {
    @SerializedName("limit")
    @Expose
    var limit: Int? = 0
    @SerializedName("offset")
    @Expose
    var offset: Int? = 0
    @SerializedName("notificationStatus")
    @Expose
    var status: String? = null
}
