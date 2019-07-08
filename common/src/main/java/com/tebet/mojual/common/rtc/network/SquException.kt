package com.tebet.mojual.common.rtc.network

class SquException : Exception {
    var errorCode: Int = 0
    var errorMessage: String? = null
    var errorMessages: List<String>? = null
    var data: Any? = null

    constructor() : super() {}

    constructor(errorCode: Int, errorMessage: String) : super(errorMessage) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    constructor(errorCode: Int, errorMessage: String, data: Any) : super(errorMessage) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
        this.data = data
    }
}
