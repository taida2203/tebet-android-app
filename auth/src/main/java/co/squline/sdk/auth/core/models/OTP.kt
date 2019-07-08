package co.squline.sdk.auth.core.models

//{"expires_in":60000,"token":"842222222222#21"}
class OTP {
    var expiresIn: Long? = null
        private set
    var token: String? = null
    var receiverPhone: String? = null
    var receiverEmail: String? = null

    fun setExpiresIn(expires_in: Long) {
        this.expiresIn = expires_in
    }
}
