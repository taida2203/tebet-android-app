package com.tebet.mojual.data.remote;

import android.content.Context;
import com.ashokvarma.gander.GanderInterceptor;
import com.tebet.mojual.common.BuildConfig;
import com.tebet.mojual.common.constant.ConfigEnv;
import com.tebet.mojual.common.handler.AppController;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by kal on 6/7/2017.
 *
 * class ini adalah konfigurasi penggunaan retrofit sebagai koneksi data.
 */

public class ApiService {
    private Context context;

    public ApiService() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    private static class ApiServiceHolder {

        static ApiService in;

        static {
            in = new ApiService();
        }
    }

    public static ApiService getInstance() {
        return ApiServiceHolder.in;
    }

    public static <E> E createServiceWP(Class<E> serviceClass) {
        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient.Builder client = httpClient.newBuilder();

        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(new AuthenticationInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(logging);
        }

        if (ConfigEnv.INSTANCE.getEnvironment() != ConfigEnv.Environment.PRODUCTION) {
            client.addInterceptor(new GanderInterceptor(AppController.getInstance().getContext())
                    .showNotification(true));
        }

        return new Retrofit.Builder()
                .baseUrl("https://blog.cakap.com/wp-json/wp/v2/")
//                .addConverterFactory(new DataWrapperConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
                .create(serviceClass);
    }

    public static <E> E createServiceOld(Class<E> serviceClass) {
        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient.Builder client = httpClient.newBuilder();

        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(new AuthenticationInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(logging);
        }
        if (ConfigEnv.INSTANCE.getEnvironment() != ConfigEnv.Environment.PRODUCTION) {
            client.addInterceptor(new GanderInterceptor(AppController.getInstance().getContext())
                    .showNotification(true));
        }

        return new Retrofit.Builder()
                .baseUrl(ConfigEnv.INSTANCE.getApiRoot() + "/v1/mobile/")
//                .addConverterFactory(new DataWrapperConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
                .create(serviceClass);
    }

    public static <E> E createServiceNew(Class<E> serviceClass) {
        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient.Builder client = httpClient.newBuilder();

        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);

        client.addInterceptor(new AuthenticationV2Interceptor());


        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(logging);
        }

        if (ConfigEnv.INSTANCE.getEnvironment() != ConfigEnv.Environment.PRODUCTION) {
            client.addInterceptor(new GanderInterceptor(AppController.getInstance().getContext())
                    .showNotification(true));
        }

        return new Retrofit.Builder()
                .baseUrl(ConfigEnv.INSTANCE.getApiRoot())
//                .addConverterFactory(new DataWrapperConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
                .create(serviceClass);
    }
}
