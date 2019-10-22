package co.sdk.auth.network

import android.text.TextUtils
import android.util.Base64
import co.sdk.auth.AuthSdk
import co.sdk.auth.AuthSdk.Companion.AUTH_LOGIN_TOKEN
import co.sdk.auth.core.models.Token
import co.common.util.PreferenceUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

internal class AuthenticationInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var language = Locale.getDefault().toString()
        if (!TextUtils.isEmpty(language) && !"in-ID".equals(language, ignoreCase = true) && !"in_ID".equals(language, ignoreCase = true)) {
            language = "en"
        } else {
            language = "in"
        }
        val currentToken = co.common.util.PreferenceUtils.getObject(AUTH_LOGIN_TOKEN, Token::class.java)
        val chainRequest = chain.request().newBuilder()
        chainRequest.addHeader("Accept-Language", language)
        chainRequest.addHeader("Content-Type", "application/json;charset=UTF-8")
        if (currentToken != null && !TextUtils.isEmpty(currentToken.accessToken) &&
                !(chain.request().url().toString().contains("/login"))) {
            chainRequest.addHeader("Authorization", currentToken.appToken)
        }
        chainRequest.addHeader("Device-Id", AuthSdk.instance.deviceId)

        val request = chainRequest.build()
        return chain.proceed(request)
    }
}