package com.ljb.module.db;

import android.database.sqlite.SQLiteDatabase;

import com.ljb.db.db.UpgradeSupport;

/**
 * Author      :meloon
 * Date        :2018/5/8
 * Description :
 */

public class DefineUpgrade extends UpgradeSupport {
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

    @Override
    public void onDownGrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
