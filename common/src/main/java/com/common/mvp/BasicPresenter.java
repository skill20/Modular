package com.common.mvp;

import android.content.Context;

import com.common.network.OKHttpUtils;

import io.reactivex.Observable;


/**
 * Create by wangqingqing
 * On 2017/12/16 12:35
 * Copyright(c) 2017 世联行
 * Description
 */
public class BasicPresenter<T extends IView> {

    protected Context mContext;
    protected T mView;
    private String tag = this.getClass().getName();
    private final OKHttpUtils okHttpUtils;

    public BasicPresenter(Context context, T view) {
        this.mView = view;
        this.mContext = context;
        okHttpUtils = OKHttpUtils.getInstance();
    }

    protected <K> Observable<K> createObservable(Observable<K> observable) {
        //创建任务
        return RxObservable.create(observable, tag);
    }

    protected <K extends BaseResponse> void subscribe(Observable<K> observable, OnResultCallBack<K> callBack) {
        RxObservable.subscribe(observable, tag, callBack);
    }

    public void destroy() {
        this.mView = null;
        this.mContext = null;
        cancelRequest();
    }

    private void cancelRequest() {
        RxObservable.dispose(tag);
    }
}
