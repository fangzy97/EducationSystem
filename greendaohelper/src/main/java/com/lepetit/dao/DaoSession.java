package com.lepetit.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lepetit.greendaohelper.ExamInfo;
import com.lepetit.greendaohelper.ScheduleInfo;

import com.lepetit.dao.ExamInfoDao;
import com.lepetit.dao.ScheduleInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig examInfoDaoConfig;
    private final DaoConfig scheduleInfoDaoConfig;

    private final ExamInfoDao examInfoDao;
    private final ScheduleInfoDao scheduleInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        examInfoDaoConfig = daoConfigMap.get(ExamInfoDao.class).clone();
        examInfoDaoConfig.initIdentityScope(type);

        scheduleInfoDaoConfig = daoConfigMap.get(ScheduleInfoDao.class).clone();
        scheduleInfoDaoConfig.initIdentityScope(type);

        examInfoDao = new ExamInfoDao(examInfoDaoConfig, this);
        scheduleInfoDao = new ScheduleInfoDao(scheduleInfoDaoConfig, this);

        registerDao(ExamInfo.class, examInfoDao);
        registerDao(ScheduleInfo.class, scheduleInfoDao);
    }
    
    public void clear() {
        examInfoDaoConfig.clearIdentityScope();
        scheduleInfoDaoConfig.clearIdentityScope();
    }

    public ExamInfoDao getExamInfoDao() {
        return examInfoDao;
    }

    public ScheduleInfoDao getScheduleInfoDao() {
        return scheduleInfoDao;
    }

}
