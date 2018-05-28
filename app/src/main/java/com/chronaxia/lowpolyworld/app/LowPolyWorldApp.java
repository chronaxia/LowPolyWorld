package com.chronaxia.lowpolyworld.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.chronaxia.lowpolyworld.model.local.greenDao.GreenDaoContext;
import com.chronaxia.lowpolyworld.model.local.greenDao.gen.DaoMaster;
import com.chronaxia.lowpolyworld.model.local.greenDao.gen.DaoSession;
import com.chronaxia.lowpolyworld.util.AudioUtil;

import java.lang.reflect.Method;

/**
 * Created by 一非 on 2018/4/8.
 */

public class LowPolyWorldApp extends Application{

    private static LowPolyWorldApp escortApp;

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AudioUtil audioUtil;
    private int x;
    private int y;

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
        audioUtil = new AudioUtil();
    }

    public AudioUtil getAudioUtil() {
        return audioUtil;
    }

    public float getX() {
        if (x == 0) {
            initWindow();
            return x;
        } else {
            return x;
        }
    }

    public float getY() {
        if (y == 0) {
            initWindow();
            return y;
        } else {
            return y;
        }
    }

    public void initWindow(){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            x = displayMetrics.widthPixels;
            y = displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void clearAndRebuildDatabase() {
        DaoMaster.dropAllTables(daoMaster.getDatabase(),true);
        DaoMaster.createAllTables(daoMaster.getDatabase(),true);
    }

}
