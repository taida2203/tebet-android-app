package com.tebet.mojual.common.network

import co.squline.sdk.auth.AuthSdk
import com.tebet.mojual.common.util.Utility
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationV2Interceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type", "application/json;charset=UTF-8")
        if (AuthSdk.instance.currentToken != null) {
            builder.addHeader("Authorization", AuthSdk.instance.currentToken!!.appToken)
        }

        if (AuthSdk.instance.deviceId != null) {
            builder.addHeader("Device-Id", AuthSdk.instance.deviceId)
        }
        builder.addHeader("Accept-Language", Utility.getInstance().deviceLanguageId)

        return chain.proceed(builder.build())
    }

}
