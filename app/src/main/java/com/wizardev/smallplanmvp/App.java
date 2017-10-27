package com.wizardev.smallplanmvp;

import android.app.Application;

import com.wizardev.smallplanmvp.data.dao.DaoMaster;
import com.wizardev.smallplanmvp.data.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   : 在此完成应用中的一些需要初始化的操作
 * version: 1.0
 */
public class App extends Application{
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "plans-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
