package com.tebet.mojual.data.remote;

import co.sdk.auth.AuthSdk;
import com.tebet.mojual.common.util.Utility;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


class AuthenticationInterceptor implements Interceptor {


    AuthenticationInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Device-Id", AuthSdk.Companion.getInstance().getDeviceId())
                .addHeader("Accept-Language", Utility.getInstance().getDeviceLanguageId())
                .build();
        return chain.proceed(request);
    }
}