package com.tebet.mojual.data.models

import retrofit2.HttpException

class NetworkError(e: Throwable?) : Throwable() {
    var errorCode: Int? = null
    var errorMessage: String? = null

    constructor(errorCode: Int? = 500, message: String?) : this(null) {
        this.errorCode = errorCode
        this.errorMessage = message
    }

    init {
        if (e is HttpException) this.errorCode = e.response().code()
        this.errorMessage = e?.message
    }
}
