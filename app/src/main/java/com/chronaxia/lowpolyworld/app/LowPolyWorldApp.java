package com.chronaxia.lowpolyworld.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.chronaxia.lowpolyworld.model.local.greenDao.GreenDaoContext;
import com.chronaxia.lowpolyworld.model.local.greenDao.gen.DaoMaster;
import com.chronaxia.lowpolyworld.model.local.greenDao.gen.DaoSession;

/**
 * Created by 一非 on 2018/4/8.
 */

public class LowPolyWorldApp extends Application{

    private static LowPolyWorldApp escortApp;

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        escortApp = this;
    }

    public static LowPolyWorldApp getInstance() {
        return escortApp;
    }

    /**
     * 由于运行时权限依赖于LoginActivity，数据库初始化在拿到权限后进行
     */
    public void initDbHelp() {
        helper = new DaoMaster.DevOpenHelper(new GreenDaoContext(this), "LowPolyWorld.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void clearAndRebuildDatabase() {
        DaoMaster.dropAllTables(daoMaster.getDatabase(),true);
        DaoMaster.createAllTables(daoMaster.getDatabase(),true);
    }

}
