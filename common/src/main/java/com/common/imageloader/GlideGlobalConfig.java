package com.common.imageloader;

import android.content.Context;

import com.baselib.app.GlobeContext;
import com.baselib.fs.DirType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.common.R;

/**
 * Create by pc-qing
 * On 2017/2/13 11:28
 * Copyright(c) 2017 世联行
 * Description
 */
public class GlideGlobalConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.common_glide_tag);
        String path = GlobeContext.getDirectoryPath(DirType.image);
        builder.setDiskCache(new DiskLruCacheFactory(path, "", Integer.MAX_VALUE));
        builder.setMemoryCache(GlideImageLoader.getInstance().memoryCache);
        builder.setBitmapPool(GlideImageLoader.getInstance().bitmapPool);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
