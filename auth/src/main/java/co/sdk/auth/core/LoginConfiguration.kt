package co.sdk.auth.core

class LoginConfiguration {

    var token: String? = null
    var otp: String? = null
    var password: String? = null
    var phone: String? = null
    var email: String? = null
    var googleClientId: String? = null

    companion object {
        val successRequestResourceCreated = 201
        val successRequest = 200
        val unauthorizedRequest = 401
    }
}
