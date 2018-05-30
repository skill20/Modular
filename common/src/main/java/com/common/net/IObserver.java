package com.common.net;

/**
 * Create by wangqingqing
 * On 2018/5/18 10:51
 * Copyright(c) 2018 极光
 * Description
 */
public interface IObserver<T> {
    void onStartRequest();

    void onSuccess(T t);

    void onError(String s);
}
