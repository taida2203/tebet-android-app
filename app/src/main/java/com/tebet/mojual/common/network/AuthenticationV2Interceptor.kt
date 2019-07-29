package com.tebet.mojual.common.network

import co.sdk.auth.AuthSdk
import com.tebet.mojual.common.util.Utility
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class AuthenticationV2Interceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type", "application/json;charset=UTF-8")
        AuthSdk.instance.currentToken?.let {
            builder.addHeader("Authorization", it.appToken)
            Timber.d("Authorization: " + it.appToken)
        }
        builder.addHeader("Device-Id", AuthSdk.instance.deviceId)
        builder.addHeader("Accept-Language", Utility.getInstance().deviceLanguageId)

        return chain.proceed(builder.build())
    }

}
