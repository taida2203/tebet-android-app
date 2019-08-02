package co.sdk.auth.core.models


class LoginInput {
    var grantType: String?= null
    var otp: String?= null
    var password: String?= null
    var phone: String?= null
    var token: String?= null
    var username: String?= null
    val clientId: String?= "tebet_mobile"
    val profileType = "CUSTOMER"
}
