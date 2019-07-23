//package com.tebet.mojual.common.util;
//
//import android.content.Context;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.module.AppGlideModule;
//import okhttp3.OkHttpClient;
//
//import javax.net.ssl.*;
//import java.io.InputStream;
//import java.security.cert.CertificateException;
//
//@GlideModule
//public final class MyAppGlideModule extends AppGlideModule {
//    @Override
//    public void registerComponents(Context context, Glide glide, Registry registry) {
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
//        registry.replace(GlideUrl.class, InputStream.class,
//                new OkHttpUrlLoader.Factory(client));
//    }
//
//    public static class UnsafeOkHttpClient {
//        public static OkHttpClient getUnsafeOkHttpClient() {
//            try {
//                // Create a trust manager that does not validate certificate chains
//                final TrustManager[] trustAllCerts = new TrustManager[] {
//                        new X509TrustManager() {
//                            @Override
//                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                            }
//
//                            @Override
//                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                            }
//
//                            @Override
//                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                                return new java.security.cert.X509Certificate[]{};
//                            }
//                        }
//                };
//
//                // Install the all-trusting trust manager
//                final SSLContext sslContext = SSLContext.getInstance("SSL");
//                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//                // Create an ssl socket factory with our all-trusting manager
//                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//
//                OkHttpClient okHttpClient = builder.build();
//                return okHttpClient;
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//}