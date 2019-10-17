package co.sdk.auth.core.models

class LoginConfiguration(var logoutWhileExpired: Boolean = true, var token: String? = null, var phone: String? = null) {
    var otp: String? = null
    var password: String? = null
    var username: String? = null
    var googleClientId: String? = null
    val authenticationType = "FIREBASE"
    val profileType = "CUSTOMER"

    companion object {
        val successRequestResourceCreated = 201
        val successRequest = 200
        val unauthorizedRequest = 401
    }
}