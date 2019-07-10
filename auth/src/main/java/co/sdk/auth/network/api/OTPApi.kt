package co.sdk.auth.network.api

import co.sdk.auth.core.models.AuthJson
import co.sdk.auth.core.models.OTP
import co.sdk.auth.core.models.RequestOTPInput
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OTPApi {

    @POST("otp")
    fun requestOTP(@Body customerDevice: RequestOTPInput): Call<AuthJson<OTP>>
}
