package com.ljb.module.db;

import android.database.sqlite.SQLiteDatabase;

import com.ljb.baselibrary.db.UpgradeSupport;

/**
 * Author      :meloon
 * Date        :2018/5/8
 * Description :
 */

public class DefineUpgrade extends UpgradeSupport {
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onUpgrade(database, oldVersion, newVersion);

    }
}
