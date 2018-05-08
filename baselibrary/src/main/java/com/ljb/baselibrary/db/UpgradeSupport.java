package com.ljb.baselibrary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Author      :meloon
 * Date        :2018/5/8
 * Description :
 */

public class UpgradeSupport {

    public UpgradeSupport() {
    }

    /**
     * 升级数据库
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    /**
     * 降级数据库
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onDownGrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
