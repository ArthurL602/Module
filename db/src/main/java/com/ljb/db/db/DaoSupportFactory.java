package com.ljb.db.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Author      :meloon
 * Date        :2018/5/7
 * Description : 数据库工厂
 */

public class DaoSupportFactory {
    private static DaoSupportFactory sDaoSupportFactory;
    private Map<String, DbSupport> mDbSupports;

    // private
    private DaoSupportFactory() {
        mDbSupports = new HashMap<>();
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


    /**
     * 创建数据库 （数据库创建和升级）
     *
     * @param rootPath
     * @return
     */
    public DbSupport createDb(String rootPath, String fileName, int version, UpgradeSupport upgradeSupport) {
        String key = rootPath + fileName;
        DbSupport dbSupport = mDbSupports.get(key);
        // 把数据库放到内存卡里面，判断是否有存储卡，6.0需要动态申请权限
        if (dbSupport == null) {
            dbSupport = new DbSupport();
            if(upgradeSupport!=null){
                dbSupport.initUpgradeSupport(upgradeSupport);
            }
            dbSupport.createDb(rootPath, fileName, version);
            mDbSupports.put(key, dbSupport);
        }
        return dbSupport;
    }


}
