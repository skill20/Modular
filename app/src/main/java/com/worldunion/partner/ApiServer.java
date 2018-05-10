package com.worldunion.partner;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by wangqingqing
 * On 2018/5/10 14:13
 * Copyright(c) 2018 极光
 * Description
 */
public interface ApiServer {
    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag, @Query("start") int start,
                                   @Query("count") int count);

}
