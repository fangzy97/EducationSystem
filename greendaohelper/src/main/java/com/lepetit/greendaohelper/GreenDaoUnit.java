package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.DaoMaster;
import com.lepetit.dao.DaoSession;
import com.lepetit.dao.ScheduleInfoDao;

import java.util.List;

abstract class GreenDaoUnit {
    private DaoSession session;

    protected void initialize(Context context, String name) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, name, null);
        DaoMaster master = new DaoMaster(openHelper.getWritableDatabase());
        session = master.newSession();
    }

    protected DaoSession getSession() {
        return session;
    }
}
