package com.tebet.mojual.di.module

import com.ashokvarma.gander.GanderInterceptor
import com.tebet.mojual.App
import com.tebet.mojual.BuildConfig
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.common.network.AuthenticationV2Interceptor
import com.tebet.mojual.data.remote.ApiGoogleHelper
import com.tebet.mojual.data.remote.ApiHelper
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule(private val baseUrl: String) {

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        ganderInterceptor: GanderInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(ganderInterceptor)
        .addInterceptor(AuthenticationV2Interceptor()).build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    @Singleton
    fun provideGanderInterceptor(): GanderInterceptor {
        return if (ConfigEnv.environment !== ConfigEnv.Environment.PRODUCTION) GanderInterceptor(App.instance.context)
            .showNotification(true) else GanderInterceptor(App.instance.context)
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    fun providesApiInterface(retrofit: Retrofit): ApiHelper = retrofit.create(
        ApiHelper::class.java
    )

    @Provides
    @Singleton
    fun providesApiGoogleInterface(okHttpClient: OkHttpClient): ApiGoogleHelper {
        return Builder().client(okHttpClient).baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(ApiGoogleHelper::class.java)
    }
}