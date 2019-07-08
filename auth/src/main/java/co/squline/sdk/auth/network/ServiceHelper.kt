package co.squline.sdk.auth.network

import co.squline.sdk.auth.AuthSdk
import com.ashokvarma.gander.GanderInterceptor
import com.google.gson.GsonBuilder
import com.tebet.mojual.sdk.auth.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceHelper {

    fun <S> createService(
            serviceClass: Class<S>): S {
        val httpClient = OkHttpClient.Builder()
        val gson = GsonBuilder()
                .create()
        val factory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
                .baseUrl(AuthSdk.instance.getBaseUrl()!!)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // really need ?
                .addConverterFactory(factory)


        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.writeTimeout(60, TimeUnit.SECONDS)

        httpClient.addInterceptor(AuthenticationInterceptor())

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(GanderInterceptor(AuthSdk.instance.context)
                    .showNotification(true))
        }

        builder.client(httpClient.build())

        val retrofit = builder.build()

        return retrofit.create(serviceClass)
    }
}
