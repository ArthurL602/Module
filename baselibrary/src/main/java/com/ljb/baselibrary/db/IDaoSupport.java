package com.ljb.baselibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description : 数据库支持类 接口
 */

public interface IDaoSupport<T> {
    /**
     * 获取一个querySupport对象
     *
     * @return
     */
    QuerySupport<T> querySupport();

    /**
     * 创建表
     *
     * @param sqLiteDatabase
     * @param clzz
     */
    void createTable(SQLiteDatabase sqLiteDatabase, Class<T> clzz);

    /**
     * 插入单条数据对象
     *
     * @param t
     * @return
     */
    long insert(T t);

    /**
     * 批量插入数据
     *
     * @param datas
     */
    void insertList(List<T> datas);

    /**
     * 按条件删除
     *
     * @param whereClause
     * @param whereArgs
     */
    int delete(String whereClause, String[] whereArgs);

    /**
     * 更新数据
     *
     * @param t
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int update(T t, String whereClause, String... whereArgs);



}
