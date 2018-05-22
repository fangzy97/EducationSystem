package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.ScheduleInfoDao;

import java.util.List;

public class ScheduleData extends GreenDaoUnit {
    private ScheduleInfoDao dao;
    private boolean initialize;
    private static ScheduleData instance;

    private ScheduleData() {
        this.initialize = false;
    }

    private static ScheduleData getInstance() {
        if (instance == null) {
            instance = new ScheduleData();
        }
        return instance;
    }

    private void _insert(String day ,String course, String teacher, String week, String time, String classroom) {
        ScheduleInfo info = new ScheduleInfo(null, day, course, teacher, week, time, classroom);
        dao.insert(info);
    }

    private void _clear() {
        dao.deleteAll();
    }

    private List<ScheduleInfo> _search() {
        return dao.loadAll();
    }

    public static void initDB(Context context, String name) {
        getInstance().initialize(context, name);
        getInstance().initialize = true;
        getInstance().dao = getInstance().getSession().getScheduleInfoDao();
    }

    public static void insert(String day, String course, String teacher, String week, String time, String classroom) {
        getInstance()._insert(day, course, teacher, week, time, classroom);
    }

    public static void clear() {
        getInstance()._clear();
    }

    public static List<ScheduleInfo> search() {
        return getInstance()._search();
    }

    public static boolean isInitialize() {
        return getInstance().initialize;
    }
}
