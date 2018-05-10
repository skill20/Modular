package com.worldunion.partner;

import com.common.mvp.BaseResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;

/**
 * Create by wangqingqing
 * On 2018/5/10 16:26
 * Copyright(c) 2018 极光
 * Description
 */
public interface CacheProvider {

    @LifeCache(duration = 30, timeUnit = TimeUnit.SECONDS)
    Observable<Book> getSearchBook(Observable<Book> oRepos, DynamicKey userName, EvictProvider evictDynamicKey);
}
