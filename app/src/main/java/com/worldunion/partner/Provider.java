package com.worldunion.partner;

import com.baselib.app.GlobeContext;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Create by wangqingqing
 * On 2018/5/10 16:34
 * Copyright(c) 2018 极光
 * Description
 */
public class Provider {

    private static CacheProvider userCacheProviders;

    public synchronized static CacheProvider getUserCache() {
        if (userCacheProviders == null) {
            userCacheProviders = new RxCache.Builder()
                    .persistence(GlobeContext.get().getApplicationContext().getExternalCacheDir(), new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(CacheProvider.class);//这些配置对应的缓存接口
        }
        return userCacheProviders;
    }
}
