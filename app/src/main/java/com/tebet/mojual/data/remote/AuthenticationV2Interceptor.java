package com.tebet.mojual.data.remote;

import androidx.annotation.NonNull;
import co.sdk.auth.AuthSdk;
import com.tebet.mojual.common.util.Utility;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


class AuthenticationV2Interceptor implements Interceptor {


    AuthenticationV2Interceptor() {
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json;charset=UTF-8");
        if (AuthSdk.Companion.getInstance().getCurrentToken() != null) {
            builder.addHeader("Authorization", AuthSdk.Companion.getInstance().getCurrentToken().getAppToken());
        }

        if (AuthSdk.Companion.getInstance().getDeviceId() != null) {
            builder.addHeader("Device-Id", AuthSdk.Companion.getInstance().getDeviceId());
        }
        builder.addHeader("Accept-Language", Utility.getInstance().getDeviceLanguageId());

        return chain.proceed(builder.build());
    }
}