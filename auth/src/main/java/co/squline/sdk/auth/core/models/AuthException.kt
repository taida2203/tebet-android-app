package co.squline.sdk.auth.core.models

class AuthException {
    var errorCode: Int = 0
    var errorMessage: String? = null
    var errorMessages: List<String>? = null

    constructor()

    constructor(errorCode: Int, errorMessage: String) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    constructor(errorCode: Int, errorMessages: List<String>) {
        this.errorCode = errorCode
        this.errorMessages = errorMessages
    }
}
