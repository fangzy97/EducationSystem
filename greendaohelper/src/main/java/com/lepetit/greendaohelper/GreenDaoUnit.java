package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.DaoMaster;
import com.lepetit.dao.DaoSession;
import com.lepetit.dao.ScheduleInfoDao;

import java.util.List;

public class GreenDaoUnit {
    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster master;
    private DaoSession session;
    private ScheduleInfoDao mDao;

    private static GreenDaoUnit instance;

    private void _initialize(Context context) {
        openHelper = new DaoMaster.DevOpenHelper(context, "user-db", null);
        master = new DaoMaster(openHelper.getWritableDatabase());
        session = master.newSession();
        mDao = session.getScheduleInfoDao();
    }

    private static GreenDaoUnit getInstance() {
        if (instance == null) {
            instance = new GreenDaoUnit();
        }
        return instance;
    }

    private void _insert(String course, String teacher, String week, String time, String classroom) {
        ScheduleInfo info = new ScheduleInfo(null, course, teacher, week, time, classroom);
        mDao.insert(info);
    }

    private List<ScheduleInfo> _search() {
        return mDao.loadAll();
    }

    //------------------------------------------------------------------------------------------------------
    //暴露给用户的方法

    public static void initialize(Context context) {
        getInstance()._initialize(context);
    }

    public static void insert(String course, String teacher, String week, String time, String classroom) {
        getInstance()._insert(course, teacher, week, time, classroom);
    }

    public static List<ScheduleInfo> search() {
        return getInstance()._search();
    }
}
