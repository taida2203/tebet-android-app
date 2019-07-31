package co.sdk.auth.core.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthJson<T>(
    @field:SerializedName("status")
    @field:Expose
    var status: String?, message: String, @SerializedName("data")
    @Expose
    var data: T? = null
) {
    @SerializedName("messages")
    @Expose
    private var messages: List<String>? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null


    init {
        this.message = message
    }

    fun getMessage(): List<String>? {
        return messages
    }

    fun setMessage(message: List<String>) {
        this.messages = message
    }
}
