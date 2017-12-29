package com.baselib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Create by wangqingqing
 * On 2017/7/11 17:43
 * Copyright(c) 2017 世联行
 * Description
 */
public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("can't create ToastUtils");
    }

    private static Toast mAppToast = null;
    public static boolean isActive = false;

    public static void showToast(Context context, int resId, boolean l) {
        showToast(context, context.getResources().getString(resId), l);
    }

    public static void showToast(Context context, String textId, boolean l) {
        if (!isActive) {
            return;
        }

        if (mAppToast == null) {
            mAppToast = Toast.makeText(context.getApplicationContext(), textId, l ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mAppToast.setText(textId);
        }
        mAppToast.show();
    }
}
