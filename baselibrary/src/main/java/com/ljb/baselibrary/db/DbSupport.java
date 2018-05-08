package com.ljb.baselibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Author      :meloon
 * Date        :2018/5/8
 * Description : 将一个数据库抽象成一个类， 一个对象
 */

public class DbSupport {
    private SQLiteDatabase mSQLiteDatabase;
    // 数据库升级帮助类
    private UpgradeSupport mUpgradeSupport;
    private SQLiteDatabase mDatabase;
    private boolean mIsFirst;

    public DbSupport() {
        mUpgradeSupport = new UpgradeSupport() {
            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

            }

            @Override
            public void onDownGrade(SQLiteDatabase database, int oldVersion, int newVersion) {

            }
        };
    }

    /**
     * 设置数据库升级对象
     *
     * @param upgradeSupport
     * @return
     */
    public DbSupport initUpgradeSupport(UpgradeSupport upgradeSupport) {
        mUpgradeSupport = upgradeSupport;
        return this;
    }

    public void createDb(String rootPath, String fileName, int version) {
        // 把数据库放到内存卡里面，判断是否有存储卡，6.0需要动态申请权限
        mDatabase = getDatabaseFromDbName(rootPath, fileName);
        int oldVersion = mDatabase.getVersion();
        int newVersion = version;
        if (oldVersion == 0) { // 第一次创建数据库
            mIsFirst = true;
        } else {
            mIsFirst = false;
            if (oldVersion < newVersion) { // 升级
                mUpgradeSupport.onUpgrade(mDatabase, oldVersion, newVersion);
            } else {// 降级
                mUpgradeSupport.onDownGrade(mDatabase, oldVersion, newVersion);
            }
        }
        mDatabase.setVersion(version);
    }

    /**
     * 获取相关 表
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> IDaoSupport getDao(Class<T> clz) {
        if (mDatabase != null) {
            IDaoSupport<T> daoSupport = new DaoSupport<>();
            if (mIsFirst) {
                // 构建表
                daoSupport.createTable(mDatabase, clz);
                return daoSupport;
            } else {
                if (DbUtils.tabIsExist(mDatabase, DbUtils.getTableName(clz))) {
                    // 得到已存在的表格
                    daoSupport.createTable(mDatabase, clz);
                    return daoSupport;
                } else {
                    throw new IllegalStateException("数据库已经存在，且该表格不在数据库中，请升级数据库再添加表格");
                }
            }
        } else {
            throw new NullPointerException("请调用init()方法，进行相关的初始化");
        }
    }

    /**
     * 根据数据库名称获取相关Database
     *
     * @param rootPath
     * @param fileName
     * @return
     */
    public SQLiteDatabase getDatabaseFromDbName(String rootPath, String fileName) {
        File dbRoot = new File(rootPath);
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, fileName);
        return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }
}
