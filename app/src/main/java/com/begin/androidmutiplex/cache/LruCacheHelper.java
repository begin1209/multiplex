package com.begin.androidmutiplex.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * LruCache的使用方法
 * @Author zhouy
 * @Date 2017-07-17
 */

public class LruCacheHelper {

    private LruCache<String, Bitmap> mLruCache;

    public LruCacheHelper(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        //设置图片缓存，用于底部状态栏中bitmap存储
        mLruCache = new LruCache<String ,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public void put(String key, Bitmap value){
        mLruCache.put(key, value);
    }

    public void get(String key){
        mLruCache.get(key);
    }
}
