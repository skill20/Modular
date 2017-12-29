package com.baselib.adapter.wrapper;

import android.support.annotation.IntDef;

/**
 * Create by wangqingqing
 * On 2017/10/31 15:19
 * Copyright(c) 2017 世联行
 * Description
 */
public class StateType {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NO_MORE = 2;
    public final static int STATE_LOADING_MORE_FAIL = 3;

    @IntDef({STATE_LOADING, STATE_COMPLETE, STATE_NO_MORE, STATE_LOADING_MORE_FAIL})
    @interface StateTypeDef {
    }

    public final int fileType;

    public StateType(@StateTypeDef int fileType) {
        this.fileType = fileType;
    }
}
