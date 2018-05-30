package com.common.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by wangqingqing
 * On 2018/5/10 13:41
 * Copyright(c) 2018 极光
 * Description
 */
public abstract class RxObserver<T> implements Observer<T>, IObserver<T> {

    private String mKey;

    public RxObserver(String key) {
        this.mKey = key;
    }

    @Override
    public void onSubscribe(Disposable d) {
        DisposableManager.getInstance().add(mKey, d);
        onStartRequest();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }


    @Override
    public void onError(Throwable e) {
        String message = e.getMessage();
        onError(message);
    }

    @Override
    public void onComplete() {

    }
}
