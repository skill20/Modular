package com.worldunion.partner;

import com.baselib.app.BaseApplication;

/**
 * Create by wangqingqing
 * On 2017/12/29 11:12
 * Copyright(c) 2017 世联行
 * Description
 */
public class App extends BaseApplication{
    @Override
    protected boolean debugLog() {
        return true;
    }

    @Override
    protected boolean enableOOM() {
        return false;
    }

    @Override
    protected boolean offLineLog() {
        return false;
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected String getMainProcess() {
        return "com.worldunion.partner";
    }

    @Override
    protected String getBaseUrl() {
        return "";
    }
}
