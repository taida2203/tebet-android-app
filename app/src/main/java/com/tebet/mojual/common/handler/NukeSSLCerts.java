package com.tebet.mojual.common.handler;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class NukeSSLCerts {
    protected static final String TAG = "NukeSSLCerts";

    public static void nuke() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession arg1) {
                    return hostname.equalsIgnoreCase("mo-jual.com") ||
                            hostname.equalsIgnoreCase("dev.api.mo-jual.com") ||
                            hostname.equalsIgnoreCase("graph.facebook.com") ||
                            hostname.equalsIgnoreCase("s3-ap-southeast-1.amazonaws.com") ||
                            hostname.equalsIgnoreCase("reports.crashlytics.com");
                }
            });
        } catch (Exception ignored) {
        }
    }
}
