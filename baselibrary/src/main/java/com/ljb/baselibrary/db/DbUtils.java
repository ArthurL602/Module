package com.ljb.baselibrary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description :
 */

public class DbUtils {

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        }
        return value;
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }

    /**
     * 判断数据库中表格是否存在
     *
     * @param database
     * @param tabName
     * @return
     */
    public static  boolean tabIsExist(SQLiteDatabase database, String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() +
                    "' ";
            cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        result = true;
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }


}
