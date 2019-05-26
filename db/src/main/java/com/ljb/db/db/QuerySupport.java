package com.ljb.db.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description : 查询支持类
 */

public class QuerySupport<T> {
    // 查询的列
    private String[] mQueryColumns;
    // 查询的条件
    private String mQuerySelection;
    // 查询的参数
    private String[] mQuerySelectionArgs;
    // 查询分组
    private String mQueryGroupBy;
    // 查询对结果集进行过滤
    private String mQueryHaving;
    // 查询排序
    private String mQueryOrderBy;
    // 查询可用于分页
    private String mQueryLimit;

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClass;

    public QuerySupport(SQLiteDatabase SQLiteDatabase, Class<T> aClass) {
        mSQLiteDatabase = SQLiteDatabase;
        mClass = aClass;
    }

    public QuerySupport columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }

    public QuerySupport groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<T> queryAll() {
        List<T> list;
        Cursor cursor = null;
        try {
            mSQLiteDatabase.beginTransaction();
            cursor = mSQLiteDatabase.query(DbUtils.getTableName(mClass), null, null, null, null, null, null, null);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
        list = cursorToList(cursor);
        return list;
    }



    /**
     * 条件查询
     *
     * @return
     */
    public List<T> query() {
        Cursor cursor = null;
        try {
            mSQLiteDatabase.beginTransaction();
            cursor = mSQLiteDatabase.query(DbUtils.getTableName(mClass), mQueryColumns, mQuerySelection,
                    mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
        clearQueryParams();
        return cursorToList(cursor);
    }
    /**
     * 将游标转换成集合
     *
     * @param cursor
     * @return 对象集合列表
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    // 得到一个对象
                    T t = mClass.newInstance();
                    // 获取所有属性
                    Field[] fields = mClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        // 获取属性名
                        String name = field.getName();
                        // 获取角标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) {
                            continue;
                        }
                        // 通过反射获取游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null) {
                            Object value = cursorMethod.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }
                            // 处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }
                            field.set(t, value);
                        }
                    }
                    // 加入集合
                    list.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            // 性能优化，关闭游标
            cursor.close();
        }
        return list;
    }
    /**
     * 清空所有参数
     */
    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelection = null;
        mQuerySelectionArgs = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }

    /**
     * 获取游标的方法 例如：getInt() ; get
     *
     * @param type
     * @return
     * @throws NoSuchMethodException
     */
    private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    /**
     * 获取方法名
     *
     * @param type
     * @return
     */
    private String getColumnMethodName(Class<?> type) {
        String typeName;
        if (type.isPrimitive()) {
            typeName = DbUtils.capitalize(type.getName());
        } else {
            typeName = type.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        Log.e("TAG", "methodName: " + methodName);
        return methodName;
    }


}
