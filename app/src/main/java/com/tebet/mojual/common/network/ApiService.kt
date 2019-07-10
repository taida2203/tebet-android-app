package com.tebet.mojual.common.network

import android.content.Context
import com.ashokvarma.gander.GanderInterceptor
import com.tebet.mojual.BuildConfig
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.common.handler.AppController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    private var context: Context? = null


    fun setContext(context: Context) {
        this.context = context
    }

    fun getContext(): Context? {
        return context
    }

    private object ApiServiceHolder {

        internal var `in`: ApiService

        init {
            `in` = ApiService()
        }
    }

    fun getInstance(): ApiService {
        return ApiServiceHolder.`in`
    }

    fun <E> createServiceWP(serviceClass: Class<E>): E {
        val httpClient = OkHttpClient()
        val client = httpClient.newBuilder()

        client.readTimeout(60, TimeUnit.SECONDS)
        client.writeTimeout(60, TimeUnit.SECONDS)
        client.addInterceptor(AuthenticationInterceptor())

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        if (ConfigEnv.environment !== ConfigEnv.Environment.PRODUCTION) {
            client.addInterceptor(
                GanderInterceptor(AppController.getInstance().context)
                    .showNotification(true)
            )
        }

        return Retrofit.Builder()
            .baseUrl("https://blog.cakap.com/wp-json/wp/v2/")
            //                .addConverterFactory(new DataWrapperConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(serviceClass)
    }


    fun <E> createServiceNew(serviceClass: Class<E>): E {
        val httpClient = OkHttpClient()
        val client = httpClient.newBuilder()

        client.readTimeout(60, TimeUnit.SECONDS)
        client.writeTimeout(60, TimeUnit.SECONDS)

        client.addInterceptor(AuthenticationV2Interceptor())


        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        if (ConfigEnv.environment !== ConfigEnv.Environment.PRODUCTION) {
            client.addInterceptor(
                GanderInterceptor(AppController.getInstance().getContext())
                    .showNotification(true)
            )
        }

        return Retrofit.Builder()
            .baseUrl(ConfigEnv.apiRoot)
            //                .addConverterFactory(new DataWrapperConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build()
            .create(serviceClass)
    }
}
