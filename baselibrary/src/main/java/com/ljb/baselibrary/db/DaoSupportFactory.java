package com.ljb.baselibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description : 数据库工厂
 */

public class DaoSupportFactory {

    private static DaoSupportFactory sDaoSupportFactory;
    // 持有外部数据库的引用
    private SQLiteDatabase mDatabase;
    // 是否是第一次创建数据库
    private boolean mIsFirst;
    // 数据库升级帮助类
    private UpgradeSupport mUpgradeSupport;

    // private
    private DaoSupportFactory() {
        mUpgradeSupport = new UpgradeSupport();
    }

    public static DaoSupportFactory getInstance() {
        if (sDaoSupportFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (sDaoSupportFactory == null) {
                    sDaoSupportFactory = new DaoSupportFactory();
                }
            }
        }
        return sDaoSupportFactory;
    }

    public DaoSupportFactory initUpgradeSupport(UpgradeSupport upgradeSupport) {
        mUpgradeSupport = upgradeSupport;
        return this;
    }

    /**
     * 创建数据库 （数据库创建和升级）
     *
     * @param rootPath
     * @return
     */
    public DaoSupportFactory createDb(String rootPath, String fileName, int version) {
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
        return this;
    }

    /**
     * 根据数据库名称获取相关Database
     *
     * @param rootPath
     * @param fileName
     * @return
     */
    public  SQLiteDatabase getDatabaseFromDbName(String rootPath, String fileName) {
        File dbRoot = new File(rootPath);
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, fileName);
        return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

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

}
