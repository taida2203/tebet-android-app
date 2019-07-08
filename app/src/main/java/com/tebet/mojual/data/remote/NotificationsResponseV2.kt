package com.tebet.mojual.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class NotificationsResponseV2 {
    @SerializedName("notifications")
    @Expose
    var notifications: List<NotificationNewResponse>? = null
    @SerializedName("total")
    @Expose
    var total: Int? = null

    companion object {
        const val STATUS_PENDING = "pending"
        const val STATUS_READ = "read"
    }
}
