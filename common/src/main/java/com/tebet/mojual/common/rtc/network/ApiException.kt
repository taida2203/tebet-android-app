package com.tebet.mojual.common.rtc.network

import android.text.TextUtils

class ApiException : Exception {
    var errorCode: Int = 0
    var errorMessage: String? = null
        get() {
            if (TextUtils.isEmpty(field) && errorMessages != null && !errorMessages!!.isEmpty()) {
                this.errorMessage = errorMessages!![0]
            }
            return field
        }
    var errorMessages: List<String>? = null

    constructor() : super()
    constructor(cause: Throwable?) : super(cause)

}

