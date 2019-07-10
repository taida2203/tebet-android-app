package co.sdk.auth.core.models

class RequestOTPInput {
    var phoneNumber: String?= null
    var type: String?= null
    private var preferEmail: Boolean = false

    fun setPreferEmail(preferEmail: Boolean) {
        this.preferEmail = preferEmail
    }
}
