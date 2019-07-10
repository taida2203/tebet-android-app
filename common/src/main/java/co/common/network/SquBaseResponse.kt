package co.common.network

import com.google.gson.annotations.SerializedName

open class SquBaseResponse<T> {
    var status: Boolean? = true
    var message: String? = null
    var data: T? = null
    var rule: Int? = null
    @SerializedName("start_date")
    var startDate: String? = null
    var end_date: String? = null
    var token: String? = null
    var to: String? = null
    var from: String? = null

    @SerializedName("mobile_message")
    var mobileMessage: String? = null

    companion object {
        const val ERROR_CODE_LOGIC = -1
        const val ERROR_CODE_RETROFIT = 0
    }
}
