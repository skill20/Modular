package com.common.base;

import android.app.Application;
import android.text.TextUtils;

import com.baselib.app.CuteLifecycleDelegate;
import com.baselib.app.GlobeContext;
import com.baselib.base.ActivityLifecycleLogger;
import com.baselib.crash.ACUncaughtExceptionHandler;
import com.baselib.fs.DirType;
import com.baselib.log.Logger;
import com.baselib.log.NLog;
import com.baselib.utils.AndroidUtils;
import com.baselib.utils.NetworkHelper;
import com.common.imageloader.ImageLoaderHelper;
import com.common.network.OKHttpUtils;


/**
 * Create by pc-qing
 * On 2017/2/15 10:37
 * Copyright(c) 2017 XunLei
 * Description
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

        GlobeContext.initInstance(this);
        initNLog();
        initNetwork();
        initCommon();
    }

    private void initNetwork() {
        NetworkHelper.sharedHelper().registerNetworkSensor(this);
    }

    private void initNLog() {
        if (!debugLog()) {
            NLog.setDebug(false, Logger.VERBOSE);
            return;
        }

        String path = GlobeContext.getDirectoryPath(DirType.crash);
        // 抓取崩溃日志
        ACUncaughtExceptionHandler handler =
                new ACUncaughtExceptionHandler(this, path, enableOOM());
        handler.registerForExceptionHandler();

        NLog.setDebug(true, Logger.VERBOSE);

        //日志写入文件
        if (offLineLog()) {
            String loggerPath = GlobeContext.getDirectoryPath(DirType.log);
            NLog.trace(Logger.TRACE_OFFLINE, loggerPath);
        } else {
            NLog.trace(Logger.TRACE_REALTIME, null);
        }
    }

    protected abstract boolean debugLog();

    protected abstract boolean enableOOM();

    protected abstract boolean offLineLog();

    protected abstract void initComponent();

    protected abstract String getMainProcess();

    protected abstract String getBaseUrl();

    private void initCommon() {

        OKHttpUtils.init(this, getBaseUrl(), debugLog());
        //主进程才执行的代码
        String currentProcessName = AndroidUtils.getCurrentProcessName(this);
        if (TextUtils.equals(currentProcessName, getMainProcess())) {
            ActivityLifecycleLogger activityLifecycleLogger =
                    new ActivityLifecycleLogger(new CuteLifecycleDelegate());
            registerActivityLifecycleCallbacks(activityLifecycleLogger);
//            PreferencesHelper.init(this);
            initComponent();
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        NLog.v("GlideGlobalConfig", "onTrimMemory level: %d", level);
        //释放图片资源
        ImageLoaderHelper.getImageLoader().trimMemory(this, level);
    }

}
