package co.sdk.auth.core.models

class LoginException : Throwable {
    var errorCode: Int = 0
    var errorMessage: String? = ""

    constructor(e: AuthException?) {
        if (e != null) {
            errorCode = e.errorCode
            errorMessage = e.errorMessage
        }
    }

    constructor(errorCode: Int, errorMessage: String?) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }
}
