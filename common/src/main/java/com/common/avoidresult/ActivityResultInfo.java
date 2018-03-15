package com.common.avoidresult;

import android.content.Intent;

/**
 * Create by wangqingqing
 * On 2018/1/17 15:49
 * Copyright(c) 2017 世联行
 * Description
 */
public class ActivityResultInfo {

    public int resultCode;
    public Intent intent;

    public ActivityResultInfo(int resultCode, Intent data) {
        this.resultCode = resultCode;
        this.intent = data;
    }

}
