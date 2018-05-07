package com.lepetit.leapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lepetit.eventmessage.FinishEvent;
import com.lepetit.eventmessage.ScheduleEvent;
import com.lepetit.greendaohelper.ScheduleData;
import com.lepetit.greendaohelper.ScheduleInfo;
import com.lepetit.schedulehelper.Schedule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

public class ScheduleActivity extends AppCompatActivity {

    private List<ScheduleInfo> scheduleInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //初始化数据库
        ScheduleData.initDB(getApplicationContext(), "Schedule.db");
        doSomeCheck();
    }

    private void doSomeCheck() {
        scheduleInfos =  ScheduleData.search();
        if (scheduleInfos.isEmpty()) {
            Schedule.getSchedule();
        } else {
            printSchedule();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetSchedule(ScheduleEvent event) {
        ScheduleData.insert(event.getDay(), event.getCourse(), event.getTeacher(),
                event.getWeek(), event.getTime(), event.getClassroom());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getScheduleFinish(FinishEvent event) {
        scheduleInfos = ScheduleData.search();
        printSchedule();
    }

    private void printSchedule() {
        for (ScheduleInfo info : scheduleInfos) {
            System.out.println(info.getDay() + " " +  info.getCourse() + " " + info.getTeacher() +
                    " " + info.getTime() + " " + info.getWeek() + " " + info.getClassroom());
        }
    }
}
