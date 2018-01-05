package com.common.mvp;

/**
 * Create by wangqingqing
 * On 2017/12/16 13:11
 * Copyright(c) 2017 世联行
 * Description
 */
public interface OnResultCallBack<T> {
    void onSuccess(T t);

    void onError(int code, String errorMsg);
}
