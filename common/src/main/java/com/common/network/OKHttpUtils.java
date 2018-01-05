package com.common.network;


import android.content.Context;
import android.webkit.CookieManager;


import com.baselib.app.GlobeContext;
import com.baselib.fs.DirType;
import com.baselib.log.NLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by pc-qing
 * On 2017/2/9 18:46
 * Copyright(c) 2017 XunLei
 * Description
 */
public class OKHttpUtils {
    private final static int CONN_TIMEOUT = 40;
    private final static int READ_TIMEOUT = 40;
    private final static int WRITE_TIMEOUT = 40;

    private volatile static OKHttpUtils sOkHttpUtils;
    private final OkHttpClient mOkHttpClient;
    private final Retrofit mRetrofit;

    private OKHttpUtils(Context context, String baseUrl, boolean debug) {

        Context tempContext = context.getApplicationContext();
        Cache cache = buildCache(tempContext);


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor());


        if (debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        setSSL(context, builder);

        OkHttpClient client = builder.build();
        mOkHttpClient = client;


        mRetrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public <T> T createInterface(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    private Cache buildCache(Context context) {
        File file = GlobeContext.getDirectory(DirType.cache);
        return new Cache(file, 10 * 1024 * 1024);
    }

    /**
     * Application 初始化
     * @param context
     * @param baseUrl
     * @param debug
     */
    public static void init(Context context, String baseUrl, boolean debug) {
        sOkHttpUtils = new OKHttpUtils(context, baseUrl, debug);
    }

    public static OKHttpUtils getInstance() {
        return sOkHttpUtils;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    private void setSSL(Context context, OkHttpClient.Builder builder) {

        try {
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("supportHttps failed", e);
        }
    }

    public SSLSocketFactory setCertificates(Context context, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    NLog.printStackTrace(e);
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //初始化keystore
            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            clientKeyStore.load(context.getAssets().open("httpsbks.bks"), "123456".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, "123456".toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            NLog.printStackTrace(e);
        }
        return null;
    }
}
