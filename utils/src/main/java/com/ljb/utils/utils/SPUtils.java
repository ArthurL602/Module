package com.ljb.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Author      :ljb
 * Date        :2018/11/1
 * Description : 土司工具类
 */

public class SPUtils {

    private static final SimpleArrayMap<String, SPUtils> SP_UTILS_MAP = new SimpleArrayMap<>();
    private SharedPreferences sp;

    /**
     * 初始化 --使用默认的构建模式和默认的spName
     * @return
     */
    public static SPUtils getInstance() {
        return getInstance("", Context.MODE_PRIVATE);
    }

    /**
     * 初始化 --使用自定义的构建模式和默认的spName
     * @param mode
     * @return
     */
    public static SPUtils getInstance(final int mode) {
        return getInstance("", mode);
    }

    /**
     * 初始化 --使用默认的构建模式和自定义的的spName
     * @param spName
     * @return
     */
    public static SPUtils getInstance(String spName) {
        return getInstance(spName, Context.MODE_PRIVATE);
    }

    /**
     * 初始化 --使用自定义的spName和自定义的构建模式
     * @param spName
     * @param mode
     * @return
     */
    public static SPUtils getInstance(String spName, final int mode) {
        if (isSpace(spName)) spName = "spUtils";
        SPUtils spUtils = SP_UTILS_MAP.get(spName);
        if (spUtils == null) {
            spUtils = new SPUtils(spName, mode);
            SP_UTILS_MAP.put(spName, spUtils);
        }
        return spUtils;
    }

    private SPUtils(final String spName) {
        sp = Utils.getApp().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private SPUtils(final String spName, final int mode) {
        sp = Utils.getApp().getSharedPreferences(spName, mode);
    }


    public void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }


    public String getString(@NonNull final String key) {
        return getString(key, "");
    }


    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }


    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }


    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }


    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value).commit();
        } else {
            sp.edit().putLong(key, value).apply();
        }
    }

    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }


    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value).commit();
        } else {
            sp.edit().putFloat(key, value).apply();
        }
    }


    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }


    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }


    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }


    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }


    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }


    public void put(@NonNull final String key, final Set<String> value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key,
                    final Set<String> value,
                    final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }


    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }


    public Set<String> getStringSet(@NonNull final String key,
                                    final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * SP 中获取所有键值对
     * @return
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * SP 中是否存在该 key
     * @param key
     * @return
     */
    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    /**
     * SP 中移除该 key
     * @param key
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }

    /**
     * SP 中移除该 key
     * @param key
     * @param isCommit
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().remove(key).apply();
        }
    }

    /**
     * SP 中清除所有数据
     */
    public void clear() {
        clear(false);
    }

    /**
     * SP 中清除所有数据
     * @param isCommit
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }

    /**
     * 判断字符串是否有空白字符串
     * @param s
     * @return
     */
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
