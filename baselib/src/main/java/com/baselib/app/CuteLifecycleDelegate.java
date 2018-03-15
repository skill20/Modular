package com.baselib.app;

import android.content.Context;

import com.baselib.base.ActivityLifecycleLogger;
import com.baselib.utils.ToastUtils;


/**
 * @author devilxie
 * @version 1.0
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
