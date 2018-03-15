package com.common.imageloader;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Create by pc-qing
 * On 2017/3/3 10:01
 * Copyright(c) 2017 世联行
 * Description
 */
public class ImageLoaderOption {

    //加载开始的图片资源
    private final int placeholder;
    //加载失败的图片资源
    private final int error;

    //重置ImageView的宽高
    private final int overrideHeight;
    private final int overrideWidth;

    //加载渐变动画
    private final boolean crossFade;
    //是否使用缓存，默认true
    private final boolean isCacheable;

    //图片显示的scaleType
    private final boolean centerCrop;
    private final boolean fitCenter;

    //图片处理
    private Transformation transformation;
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.RESULT;
    private Priority priority = null;
    private boolean dontAnimate;

    private ImageLoaderOption(Builder builder) {
        this.placeholder = builder.placeholder;
        this.error = builder.error;
        this.overrideHeight = builder.overrideHeight;
        this.overrideWidth = builder.overrideWidth;
        this.crossFade = builder.crossFade;
        this.isCacheable = builder.isCacheable;
        this.centerCrop = builder.centerCrop;
        this.fitCenter = builder.fitCenter;
        this.transformation = builder.transformation;
        this.diskCacheStrategy = builder.diskCacheStrategy;
        this.priority = builder.priority;
        this.dontAnimate = builder.dontAnimate;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public boolean isError() {
        return error != -1;
    }

    public boolean isPlaceholder() {
        return placeholder != -1;
    }

    public int getError() {
        return error;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public boolean skipMemoryCache() {
        return isCacheable;
    }

    public boolean centerCrop() {
        return centerCrop;
    }

    public boolean fitCenter() {
        return fitCenter;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public DiskCacheStrategy getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public boolean resized() {
        return overrideWidth > 0 && overrideHeight > 0;
    }

    public int getOverrideWidth() {
        return overrideWidth;
    }

    public int getOverrideHeight() {
        return overrideHeight;
    }

    public boolean dontAnimate() {
        return dontAnimate;
    }

    public static class Builder {
        private int placeholder = -1;
        private int error = -1;

        private int overrideHeight = -1;
        private int overrideWidth = -1;

        private boolean crossFade = true;
        private boolean isCacheable = true;

        private boolean centerCrop = false;
        private boolean fitCenter = false;

        private Transformation transformation = null;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.SOURCE;
        private Priority priority = null;
        public boolean dontAnimate;

        public Builder placeholder(int resourceId) {
            this.placeholder = resourceId;
            return this;
        }

        public Builder error(int resourceId) {
            this.error = resourceId;
            return this;
        }


        public Builder override(int width, int height) {
            this.overrideWidth = width;
            this.overrideHeight = height;
            return this;
        }

        public Builder crossFade(boolean b) {
            this.crossFade = b;
            return this;
        }

        public Builder skipMemoryCache(boolean skip) {
            this.isCacheable = !skip;
            return this;
        }

        public Builder centerCrop() {
            this.centerCrop = true;
            return this;
        }

        public Builder fitCenter() {
            this.fitCenter = true;
            return this;
        }

        public Builder transformation(Transformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public ImageLoaderOption build() {
            return new ImageLoaderOption(this);
        }

        public Builder diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder dontAnimate() {
            this.dontAnimate = true;
            return this;
        }
    }
}
