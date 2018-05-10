package com.worldunion.partner;


import com.common.base.BaseApplication;
import com.common.net.HttpManager;
import com.common.network.OKHttpUtils;

/**
 * Create by wangqingqing
 * On 2017/12/29 11:12
 * Copyright(c) 2017 世联行
 * Description
 */
public class App extends BaseApplication {
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
//        OKHttpUtils.init(this,getBaseUrl(),true);
        HttpManager.init(this,getBaseUrl(),true);
    }

    @Override
    protected String getMainProcess() {
        return "com.worldunion.partner";
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.douban.com/v2/";
    }
}
