package com.common.net;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by wangqingqing
 * On 2018/5/10 15:09
 * Copyright(c) 2018 极光
 * Description
 */
public class HttpManager {

    private final static int CONN_TIMEOUT = 40;
    private final static int READ_TIMEOUT = 40;
    private final static int WRITE_TIMEOUT = 40;

    private volatile static HttpManager sHttpManager;
    private Retrofit mRetrofit;

    public HttpManager(Context context, String baseUrl, boolean debug) {


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        if (debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }


    public static HttpManager getInstance() {
        return sHttpManager;
    }

    /**
     * Application 初始化
     *
     * @param context
     * @param baseUrl
     * @param debug
     */
    public static void init(Context context, String baseUrl, boolean debug) {
        sHttpManager = new HttpManager(context, baseUrl, debug);
    }

    public <T> T createInterface(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }


    public <T> void toSubscribe(Observable<T> o, RxObserver<T> s) {
        o.compose(RxSchedulers.<T>ioToMain()).subscribe(s);
    }


    public void clear(String key) {
        DisposableManager.getInstance().clear(key);
    }

    public void clear() {
        DisposableManager.getInstance().clear();
    }
}
