package com.common.net;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangqingqing
 * On 2018/5/10 13:20
 * Copyright(c) 2018 极光
 * Description
 */
public class RxSchedulers {

    private RxSchedulers() {
        throw new UnsupportedOperationException("can't create RxSchedulers instance");
    }

    /**
     * io线程变换main线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> ioToMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
