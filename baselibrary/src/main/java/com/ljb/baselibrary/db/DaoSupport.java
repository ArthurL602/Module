package com.ljb.baselibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description : 数据库支持类 接口的 实现类
 */

public class DaoSupport<T> implements IDaoSupport<T> {
    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClass;
    private static final Object[] mPutMethodArgs = new Object[2];
    private Map<String, Method> mPutMethods = new HashMap<>();

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase, Class<T> clzz) {
        mClass = clzz;
        mSQLiteDatabase = sqLiteDatabase;
        // 创建表
        StringBuffer sb = new StringBuffer();
        // sqlite 语句
        sb.append("create table if not exists ").append(DbUtils.getTableName(clzz)).append(" (id integer primary key " +
                "" + "autoincrement, ");
        // 对象的属性，同过反射去获取
        Field[] fields = mClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String name = field.getName();// 获取属性名
            String type = field.getType().getSimpleName();// 得到的是 int String boolean 等类型
            // type 需要进行转化 ， int --> integer String --> text
            sb.append(name).append(DbUtils.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")"); // 将最后的", "  -->  ")"
        // 如果表不存在就创建表
        mSQLiteDatabase.execSQL(sb.toString());
    }

    /**
     * 插入单条数据
     *
     * @param t
     * @return
     */
    @Override
    public long insert(T t) {
        // 使用的还是原生的 使用方式，只是封装了一下
        ContentValues values = contentValuesByObj(t);
        return mSQLiteDatabase.insert(DbUtils.getTableName(mClass), null, values);
    }

    /**
     * 批量插入数据
     *
     * @param datas
     */
    @Override
    public void insertList(List<T> datas) {
        // 优化插入速度： 开启事务
        try {
            mSQLiteDatabase.beginTransaction();
            for (T data : datas) {
                insert(data);
            }
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    /**
     * 按条件删除
     *
     * @param whereClause
     * @param whereArgs
     */
    @Override
    public int delete(String whereClause, String[] whereArgs) {
        int count = 0;
        try {
            mSQLiteDatabase.beginTransaction();
            count = mSQLiteDatabase.delete(DbUtils.getTableName(mClass), whereClause, whereArgs);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
        return count;
    }

    /**
     * 更新数据
     *
     * @param t
     * @param whereClause
     * @param whereArgs
     * @return
     */
    @Override
    public int update(T t, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(t);
        int count = 0;
        try {
            mSQLiteDatabase.beginTransaction();
            count = mSQLiteDatabase.update(DbUtils.getTableName(mClass), values, whereClause, whereArgs);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
        return count;
    }
    /**
     * 得到一个querySupport
     *
     * @return
     */
    public QuerySupport<T> querySupport() {
        return new QuerySupport<T>(mSQLiteDatabase, mClass);
    }


    /**
     * 构建ContentValues
     *
     * @param t
     * @return
     */
    private ContentValues contentValuesByObj(T t) {
        ContentValues values = new ContentValues();
        // 封装values
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                // 对象属性值 和 属性名
                String key = field.getName();
                Object value = field.get(t);
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                // 使用反射调用cotentvalues的 put()方法
                String fileTypeName = field.getType().getName();
                Method method = mPutMethods.get(fileTypeName);
                if (method == null) {
                    method = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(fileTypeName, method);
                }
                method.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }
}
