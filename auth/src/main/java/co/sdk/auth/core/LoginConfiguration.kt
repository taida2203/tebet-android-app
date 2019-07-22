package co.sdk.auth.core

class LoginConfiguration {

    var token: String? = null
    var otp: String? = null
    var password: String? = null
    var phone: String? = null
    var username: String? = null
    var googleClientId: String? = null
    val authenticationType = "ACCOUNT_KIT"
    val profileType = "CUSTOMER"

    companion object {
        val successRequestResourceCreated = 201
        val successRequest = 200
        val unauthorizedRequest = 401
    }
}