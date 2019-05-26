package com.ljb.db.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Author      :meloon
 * Date        :2018/5/8
 * Description : 数据库升级模板类
 */

public abstract class UpgradeSupport {

    public UpgradeSupport() {
    }

    /**
     * 升级数据库
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public abstract void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);

    /**
     * 降级数据库
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public abstract void onDownGrade(SQLiteDatabase database, int oldVersion, int newVersion);
}
