package com.ljb.storage.storagefactory;


import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Author      :ljb
 * Date        :2018/8/3
 * Description : 内存存储
 */
public class MemoryIOHandler implements IOHandler {
    LruCache<String, Object> mLruCache;
    LruCache<String, Bitmap> mBitmapLruCache;

    public MemoryIOHandler() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int memory = (int) (maxMemory / 1024 / 8);
        mLruCache = new LruCache<>(memory);
        mBitmapLruCache = new LruCache<String, Bitmap>(memory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void save(String key, String value) {
        mLruCache.put(key, value);
    }

    @Override
    public void save(String key, double value) {
        mLruCache.put(key, value);
    }

    @Override
    public void save(String key, int value) {
        mLruCache.put(key, value);
    }

    @Override
    public void save(String key, long value) {
        mLruCache.put(key, value);
    }

    @Override
    public void save(String key, boolean value) {
        mLruCache.put(key, value);
    }

    @Override
    public void save(String key, Object value) {
        mLruCache.put(key, value);
    }

    public void save(String key, Bitmap bitmap) {
        mBitmapLruCache.put(key, bitmap);
    }

    @Override
    public void save(String key, float value) {
        mLruCache.put(key, value);
    }

    @Override
    public String getString(String key) {
        return (String) mLruCache.get(key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return (double) mLruCache.get(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return (int) mLruCache.get(key);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return (long) mLruCache.get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return (boolean) mLruCache.get(key);
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        return mLruCache.get(key);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return (float) mLruCache.get(key);
    }

    public Bitmap getBitmap(String key) {
        return mBitmapLruCache.get(key);
    }

    @Override
    public void clearString(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearDouble(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearInt(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearLong(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearBoolean(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearObject(String key) {
        mLruCache.remove(key);
    }

    @Override
    public void clearFloat(String key) {
        mLruCache.remove(key);
    }


    public void clearBitmap(String key) {
        mBitmapLruCache.remove(key);

    }
}
