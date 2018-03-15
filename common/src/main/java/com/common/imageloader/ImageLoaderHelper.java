package com.common.imageloader;

/**
 * Create by pc-qing
 * On 2017/2/13 10:18
 * Copyright(c) 2017 世联行
 * Description
 */
public class ImageLoaderHelper {


    private static final String TYPE_GLIDE = "glide";
    private static final String DEFAULT_TYPE = TYPE_GLIDE;

    private ImageLoaderHelper() {
        throw new UnsupportedOperationException("can't create ImageLoaderHelper instance");
    }

    public static ImageLoader getImageLoader() {
        return realImageLoader(DEFAULT_TYPE);
    }

    private static ImageLoader realImageLoader(String defaultType) {
        return GlideImageLoader.getInstance();
    }
}
