package co.sdk.auth.network

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var urlMethod = ""
        var urlPath = ""
        var urlQuery: String? = ""
        if (chain.request() != null) {
            urlMethod = chain.request().method()
            if (chain.request().url() != null) {
                urlPath = "https://" + chain.request().url().host() + chain.request().url().encodedPath()
                urlQuery = chain.request().url().encodedQuery()
            }
        }
        var request = chain.request()
        var signature: String? = null
        if ("GET".equals(urlMethod, ignoreCase = true) && urlPath.contains("/login") ||
                "POST".equals(urlMethod, ignoreCase = true) && urlPath.contains("/login") ||
                "POST".equals(urlMethod, ignoreCase = true) && urlPath.contains("/otp")) { // check if is login api, then insert signature
            signature = urlQuery?.let { SignatureGenerator.generate(urlMethod, urlPath, it) }
            request = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", signature!!)
                    .build()
        }

        return chain.proceed(request)
    }

}
