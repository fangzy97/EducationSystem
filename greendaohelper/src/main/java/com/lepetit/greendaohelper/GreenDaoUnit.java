package com.lepetit.greendaohelper;

import android.content.Context;

import com.lepetit.dao.DaoMaster;
import com.lepetit.dao.DaoSession;
import com.lepetit.dao.ExamInfoDao;
import com.lepetit.dao.GradeInfoDao;
import com.lepetit.dao.ScheduleInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

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

    private List<ScheduleInfo> _querySchedule(String course) {
		QueryBuilder<ScheduleInfo> qb = scheduleInfoDao.queryBuilder();
		qb.where(ScheduleInfoDao.Properties.Course.eq(course));
		return qb.list();
	}

    private List<ScheduleInfo> _getSchedule() {
        return scheduleInfoDao.loadAll();
    }

    private List<ExamInfo> _getExam() {
		QueryBuilder<ExamInfo> qb = examInfoDao.queryBuilder();
		qb.orderAsc(ExamInfoDao.Properties.Time);
		return qb.list();
    }

    private List<GradeInfo> _getGrade() {
        return gradeInfoDao.loadAll();
    }

    private List<GradeInfo> _queryGrade(String year) {
    	QueryBuilder<GradeInfo> qb = gradeInfoDao.queryBuilder();
    	qb.where(GradeInfoDao.Properties.Year.eq(year));
    	return  qb.list();
	}

    private void _insertSchedule(
            String day, String course, String teacher, String week, String time, String classroom, String lastWeek) {
        ScheduleInfo info = new ScheduleInfo(null, day, course, teacher, week, time, classroom, lastWeek);
        scheduleInfoDao.insertOrReplace(info);
    }

    private void _insertExam(String course, String time, String classroom, String seat) {
        ExamInfo info = new ExamInfo(null, course, time, classroom, seat);
        examInfoDao.insert(info);
    }

    private void _insertGrade(String year, String course, String score, String credit) {
        GradeInfo info = new GradeInfo(null, year, course, score, credit);
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

    public static List<ScheduleInfo> querySchedule(String course) {
    	return getInstance()._querySchedule(course);
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

    public static List<GradeInfo> queryGrade(String year) {
    	return getInstance()._queryGrade(year);
	}

    public static void insertSchedule(
            String day, String course, String teacher, String week, String time, String classroom, String lastWeek) {
        getInstance()._insertSchedule(day, course, teacher, week, time, classroom, lastWeek);
    }

    public static void insertExam(String course, String time, String classroom, String seat) {
        getInstance()._insertExam(course, time, classroom, seat);
    }

    public static void insertGrade(String year, String course, String score, String credit) {
        getInstance()._insertGrade(year, course, score, credit);
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
