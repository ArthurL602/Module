package com.ljb.storage.storagefactory;

import java.io.InputStream;

/**
 * Author      :ljb
 * Date        :2018/8/3
 * Description :
 */
public abstract class AbstractDiskIOHandler implements IOHandler{

   public  abstract void save(String key, InputStream inputStream);
public abstract InputStream get(String key);
    public abstract void  clear(String key);
    @Override
    public void save(String key, String value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, double value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, int value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, long value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, boolean value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, Object value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void save(String key, float value) {
        throw new RuntimeException("不支持");
    }

    @Override
    public String getString(String key) {
        throw new RuntimeException("不支持");

    }

    @Override
    public double getDouble(String key, double defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public int getInt(String key, int defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public long getLong(String key, long defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearString(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearDouble(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearInt(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearLong(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearBoolean(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearObject(String key) {
        throw new RuntimeException("不支持");
    }

    @Override
    public void clearFloat(String key) {
        throw new RuntimeException("不支持");
    }
}
