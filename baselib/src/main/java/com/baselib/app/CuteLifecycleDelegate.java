package com.worldunion.partner.app;

import android.content.Context;

import com.worldunion.library.base.ActivityLifecycleLogger;
import com.worldunion.library.utils.ToastUtils;


/**
 * Create by pc-qing
 * On 2017/2/15 11:56
 * Copyright(c) 2017 XunLei
 * Description
 */
public class CuteLifecycleDelegate implements ActivityLifecycleLogger.ApplicationLifecycleDelegate {
    @Override
    public int backgroundTickDelay() {
        return 0;
    }

    @Override
    public void willEnterBackground(Context context) {

    }

    @Override
    public void enterBackground(Context context) {
        ToastUtils.isActive = false;
    }

    @Override
    public void becomeActiveFromSuspend(Context context) {

    }

    @Override
    public void enterForeground(Context context) {
        ToastUtils.isActive = true;
    }
}
