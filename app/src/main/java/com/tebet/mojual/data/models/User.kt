package com.tebet.mojual.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("userId")
    @Expose
    var userId: Int? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
}
