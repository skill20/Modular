package com.common.mvp;//package com.worldunion.library.mvp;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.baselib.log.NLog;

import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangqingqing
 * On 2017/12/16 11:21
 * Copyright(c) 2017 世联行
 * Description
 */
public final class RxObservable {

    private static final String TAG = "RxObservable";
    private static final String TOKEN_INVALID = "0004";
    private static final String RESPONSE_CODE_OK = "0001";

    /**
     * 全局变量，使用tag标识保存Disposable集合
     * Disposable?Observer(观察者)与Observable(被观察者)切断链接
     */
    private static final WeakHashMap<String, CompositeDisposable> sObservableDisposableList = new WeakHashMap<>();

    public RxObservable() {
    }

    /**
     * 创建被观察者，如retrofit集合rxJava返回的网络请求，
     * 此方法用于事件在初始化时进行处理，把此事件保存到sObservableDisposableList集合中，
     */
    public static <T> Observable<T> create(@NonNull Observable<T> observable, @NonNull final String tag) {
        return observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                collectDispose(disposable, tag);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static void collectDispose(Disposable disposable, String tag) {
        CompositeDisposable disposables = RxObservable.sObservableDisposableList.get(tag);
        if (disposables == null) {
            disposables = new CompositeDisposable();
            RxObservable.sObservableDisposableList.put(tag, disposables);
        }
        disposables.add(disposable);
    }

    public static <K extends BaseResponse> void subscribe(@NonNull Observable<K> observable, @NonNull String tag, @NonNull final OnResultCallBack<K> callBack) {
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<K>() {
                    @Override
                    public void accept(K t) {

                        boolean b = true;
                        String msg;
                        do {
                            if (t == null) {
                                msg = "response data is empty";
                                break;
                            }

                            String code = t.code;
                            msg = t.message;

                            if (TextUtils.equals(TOKEN_INVALID, code)) {
                                break;
                            }

                            if (TextUtils.equals(RESPONSE_CODE_OK, code)) {
                                b = false;
                                callBack.onSuccess(t);
                            }


                        } while (false);

                        if (b) {
                            callBack.onError(-1, msg);
                        }

                        NLog.i(TAG, "response accept：msg=%s", msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(-1, throwable.getMessage());
                    }
                });
        collectDispose(disposable, tag);
    }

    /**
     * 释放所有资源
     */
    public static void dispose() {
        for (CompositeDisposable disposables : sObservableDisposableList.values()) {
            disposables.clear();
        }
        sObservableDisposableList.clear();

    }

    /**
     * 根据tag标识进行释放资源
     *
     * @param tag tag
     */
    public static void dispose(@NonNull String tag) {
        if (sObservableDisposableList.containsKey(tag)) {
            CompositeDisposable disposables = sObservableDisposableList.get(tag);
            disposables.clear();
            sObservableDisposableList.remove(tag);
        }

    }
}
