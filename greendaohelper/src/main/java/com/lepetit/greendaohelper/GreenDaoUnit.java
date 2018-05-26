package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.DaoMaster;
import com.lepetit.dao.DaoSession;
import com.lepetit.dao.ExamInfoDao;
import com.lepetit.dao.GradeInfoDao;
import com.lepetit.dao.ScheduleInfoDao;

import java.util.List;

public class GreenDaoUnit {
    private static GreenDaoUnit instance;
    private ScheduleInfoDao scheduleInfoDao;
    private ExamInfoDao examInfoDao;
    private GradeInfoDao gradeInfoDao;
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
        DaoSession session = master.newSession();
        scheduleInfoDao = session.getScheduleInfoDao();
        examInfoDao = session.getExamInfoDao();
        gradeInfoDao = session.getGradeInfoDao();
        initialize = true;
    }

    private boolean _isInitialize() {
        return initialize;
    }

    private void _clearAll() {
        _clearSchedule();
        _clearExam();
        _clearGrade();
    }

    private void _clearSchedule() {
        scheduleInfoDao.deleteAll();
    }

    private void _clearExam() {
        examInfoDao.deleteAll();
    }

    private void _clearGrade() {
        gradeInfoDao.deleteAll();
    }

    private List<ScheduleInfo> _getSchedule() {
        return scheduleInfoDao.loadAll();
    }

    private List<ExamInfo> _getExam() {
        return examInfoDao.loadAll();
    }

    private List<GradeInfo> _getGrade() {
        return gradeInfoDao.loadAll();
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

    private void _insertGrade(String course, String score, String credit) {
        GradeInfo info = new GradeInfo(null, course, score, credit);
        gradeInfoDao.insert(info);
    }

    private boolean _isScheduleEmpty() {
        return _getSchedule().isEmpty();
    }

    private boolean _isExamEmpty() {
        return _getExam().isEmpty();
    }

    private boolean _isGradeEmpty() {
        return _getGrade().isEmpty();
    }

    public static void initialize(Context context, String name) {
        getInstance()._initialize(context, name);
    }

    public static boolean isInitialize() {
        return getInstance()._isInitialize();
    }

    public static void clearAll() {
        getInstance()._clearAll();
    }

    public static void clearSchedule() {
        getInstance()._clearSchedule();
    }

    public static void clearExam() {
        getInstance()._clearExam();
    }

    public static void clearGrade() {
        getInstance()._clearGrade();
    }

    public static List<ScheduleInfo> getSchedule() {
        return getInstance()._getSchedule();
    }

    public static List<ExamInfo> getExam() {
        return getInstance()._getExam();
    }

    public static List<GradeInfo> getGrade() {
        return getInstance()._getGrade();
    }

    public static void insertSchedule(
            String day, String course, String teacher, String week, String time, String classroom) {
        getInstance()._insertSchedule(day, course, teacher, week, time, classroom);
    }

    public static void insertExam(String course, String time, String classroom, String seat) {
        getInstance()._insertExam(course, time, classroom, seat);
    }

    public static void insertGrade(String course, String score, String credit) {
        getInstance()._insertGrade(course, score, credit);
    }

    public static boolean isScheduleEmpty() {
        return getInstance()._isScheduleEmpty();
    }

    public static boolean isExamEmpty() {
        return getInstance()._isExamEmpty();
    }

    public static boolean isGradeEmpty() {
        return getInstance()._isGradeEmpty();
    }
}
