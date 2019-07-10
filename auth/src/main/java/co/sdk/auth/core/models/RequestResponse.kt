package co.sdk.auth.core.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class RequestResponse<T> {

    @SerializedName("status")
    @Expose
    var status: Any? = null
    @SerializedName("messages")
    @Expose
    var messages: List<String> = ArrayList()
    @SerializedName("data")
    @Expose
    var data: T? = null
}