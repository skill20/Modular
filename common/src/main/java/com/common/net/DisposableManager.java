package com.common.net;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create by wangqingqing
 * On 2018/5/10 13:26
 * Copyright(c) 2018 极光
 * Description
 */
public class DisposableManager {

    private static class Holder {
        private static final DisposableManager INSTANCE = new DisposableManager();
    }

    public static DisposableManager getInstance() {
        return Holder.INSTANCE;
    }

    private final WeakHashMap<String, CompositeDisposable> mDisposableMap = new WeakHashMap<>();
    private final CompositeDisposable mComDisposable = new CompositeDisposable();

    private DisposableManager() {

    }


    public void add(@NonNull String key, Disposable disposable) {
        CompositeDisposable disposables = mDisposableMap.get(key);
        if (disposables == null) {
            disposables = new CompositeDisposable();
            mDisposableMap.put(key, disposables);
        }
        disposables.add(disposable);
    }

    public void add(@NonNull Disposable disposable) {
        mComDisposable.add(disposable);
    }

    public void delete(@NonNull Disposable disposable) {
        mComDisposable.delete(disposable);
    }
    /**
     * 根据key释放
     */
    public void clear(@NonNull String key) {
        if (mDisposableMap.containsKey(key)) {
            CompositeDisposable disposables = mDisposableMap.get(key);
            disposables.clear();
            mDisposableMap.remove(key);
        }
    }

    /**
     * 释放所有资源
     */
    public void clear() {
        for (CompositeDisposable disposables : mDisposableMap.values()) {
            disposables.clear();
        }
        mDisposableMap.clear();

        mComDisposable.clear();
    }

}
