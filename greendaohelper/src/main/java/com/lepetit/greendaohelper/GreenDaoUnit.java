package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.DaoMaster;
import com.lepetit.dao.DaoSession;
import com.lepetit.dao.ExamInfoDao;
import com.lepetit.dao.ScheduleInfoDao;

import java.util.List;

public class GreenDaoUnit {
    private static GreenDaoUnit instance;
    private DaoSession session;
    private ScheduleInfoDao scheduleInfoDao;
    private ExamInfoDao examInfoDao;
    private boolean initialize;

    private GreenDaoUnit() {
        initialize = false;
    }

    private static GreenDaoUnit getInstance() {
        if (instance == null) {
            instance = new GreenDaoUnit();
        }
        return instance;
    }

    private void _initialize(Context context, String name) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, name, null);
        DaoMaster master = new DaoMaster(openHelper.getWritableDatabase());
        session = master.newSession();
        scheduleInfoDao = session.getScheduleInfoDao();
        examInfoDao = session.getExamInfoDao();
        initialize = true;
    }

    private boolean _isInitialize() {
        return initialize;
    }

    private void _clear() {
        scheduleInfoDao.deleteAll();
        examInfoDao.deleteAll();
    }

    private List<ScheduleInfo> _getSchedule() {
        return scheduleInfoDao.loadAll();
    }

    private List<ExamInfo> _getExam() {
        return examInfoDao.loadAll();
    }

    private void _insertSchedule(
            String day, String course, String teacher, String week, String time, String classroom) {
        ScheduleInfo info = new ScheduleInfo(null, day, course, teacher, week, time, classroom);
        scheduleInfoDao.insert(info);
    }

    private void _insertExam(String course, String time, String classroom, String seat) {
        ExamInfo info = new ExamInfo(null, course, time, classroom, seat);
        examInfoDao.insert(info);
    }

    private boolean _isScheduleEmpty() {
        return _getSchedule().isEmpty();
    }

    private boolean _isExamEmpty() {
        return _getExam().isEmpty();
    }

    public static void initialize(Context context, String name) {
        getInstance()._initialize(context, name);
    }

    public static boolean isInitialize() {
        return getInstance()._isInitialize();
    }

    public static void clear() {
        getInstance()._clear();
    }

    public static List<ScheduleInfo> getSchedule() {
        return getInstance()._getSchedule();
    }

    public static List<ExamInfo> getExam() {
        return getInstance()._getExam();
    }

    public static void insertSchedule(
            String day, String course, String teacher, String week, String time, String classroom) {
        getInstance()._insertSchedule(day, course, teacher, week, time, classroom);
    }

    public static void insertExam(String course, String time, String classroom, String seat) {
        getInstance()._insertExam(course, time, classroom, seat);
    }

    public static boolean isScheduleEmpty() {
        return getInstance()._isScheduleEmpty();
    }

    public static boolean isExamEmpty() {
        return getInstance()._isExamEmpty();
    }
}
