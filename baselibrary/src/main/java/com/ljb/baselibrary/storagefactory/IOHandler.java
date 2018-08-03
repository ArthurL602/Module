package com.ljb.baselibrary.storagefactory;

/**
 * Author      :ljb
 * Date        :2018/8/2
 * Description : 数据存储的一些规范
 */
public interface IOHandler {
    /***************** save data start *******************/
    void save(String key, String value);
    void save(String key, double value);
    void save(String key, int value);
    void save(String key, long value);
    void save(String key, boolean value);
    void save(String key, Object value);
    void save(String key, float value);
    /***************** save data end *******************/

    /***************** get data start *******************/
    String getString(String key);
    double getDouble(String key, double defaultValue);
    int getInt(String key, int defaultValue);
    long getLong(String key, long defaultValue);
    boolean getBoolean(String key, boolean defaultValue);
    Object getObject(String key, Object defaultValue);
    float getFloat(String key, float defaultValue);
    /***************** get data end *******************/

    /****************** clear data start *******************************/
    void clearString(String key);
    void clearDouble(String key);
    void clearInt(String key);
    void clearLong(String key);
    void clearBoolean(String key);
    void clearObject(String key);
    void clearFloat(String key);

    /******************* clear data end *******************************/
}
