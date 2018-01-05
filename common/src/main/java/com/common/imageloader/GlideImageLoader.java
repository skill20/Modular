package com.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.baselib.log.NLog;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Create by pc-qing
 * On 2017/2/13 10:25
 * Copyright(c) 2017 XunLei
 * Description
 */
public class GlideImageLoader implements ImageLoader {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    final LruResourceCache memoryCache;
    final LruBitmapPool bitmapPool;

    private GlideImageLoader() {
        memoryCache = new InnerMemoryCache(MAX_MEMORY_CACHE_SIZE);
        bitmapPool = new LruBitmapPool(GlideImageLoader.MAX_MEMORY_CACHE_SIZE);
    }

    public static GlideImageLoader getInstance() {
        return Holder._instance;
    }

    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public File downloadImg(Context context, String url, int width, int height) {
        try {
            return Glide.with(context.getApplicationContext()).load(url).downloadOnly(width, height).get();
        } catch (InterruptedException | ExecutionException e) {
            NLog.printStackTrace(e);
        }
        return null;
    }

    @Override
    public void display(int resId, ImageView imageView) {
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(resId);
        loadOptions(dtr, new ImageLoaderOption.Builder().build()).into(imageView);
    }

    @Override
    public void display(String url, ImageView imageView) {
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(url);
        loadOptions(dtr, new ImageLoaderOption.Builder().build()).into(imageView);
    }

    @Override
    public void display(String url, ImageView imageView, ImageLoaderOption option) {
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(url);
        loadOptions(dtr, option).into(imageView);
    }

    public void display(Context context, String url, ImageView imageView, BitmapTransformation transform) {
        Glide.with(context).load(url).bitmapTransform(transform).into(imageView);
    }

    @Override
    public Bitmap loadBitmap(Context context, String url, int width, int height) {
        try {
            return Glide.with(context).load(url).asBitmap().into(width, height).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DrawableTypeRequest loadOptions(DrawableTypeRequest dtr, ImageLoaderOption option) {
        if (option == null) {
            return dtr;
        }

        if (option.resized()) {
            dtr.override(option.getOverrideWidth(), option.getOverrideHeight());
        }

        if (option.centerCrop()) {
            dtr.centerCrop();
        } else if (option.fitCenter()) {
            dtr.fitCenter();
        }

        if (option.isCrossFade()) {
            dtr.crossFade();
        }

        if (option.isError()) {
            dtr.error(option.getError());
        }

        if (option.isPlaceholder()) {
            dtr.placeholder(option.getPlaceholder());
        }

        if (option.getTransformation() != null) {
            dtr.bitmapTransform(option.getTransformation());
        }

        if (option.skipMemoryCache()) {
            dtr.skipMemoryCache(true);
        }

        if (option.getDiskCacheStrategy() != null) {
            dtr.diskCacheStrategy(option.getDiskCacheStrategy());
        }

        if (option.getPriority() != null) {
            dtr.priority(option.getPriority());
        }

        if (option.dontAnimate()) {
            dtr.dontAnimate();
        }

        return dtr;
    }

    private static class Holder {
        private static GlideImageLoader _instance = new GlideImageLoader();
    }

    private static class InnerMemoryCache extends LruResourceCache {

        /**
         * Constructor for LruResourceCache.
         *
         * @param size The maximum size in bytes the in memory cache can use.
         */
        public InnerMemoryCache(int size) {
            super(size);
        }

        @Override
        protected void onItemEvicted(Key key, Resource<?> item) {
            super.onItemEvicted(key, item);
        }
    }

}
