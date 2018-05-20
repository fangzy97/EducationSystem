package com.lepetit.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;

import com.lepetit.eventmessage.ScheduleEvent;
import com.lepetit.greendaohelper.ScheduleData;
import com.lepetit.greendaohelper.ScheduleInfo;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.schedulehelper.Schedule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleActivity extends AppCompatActivity {

    private List<ScheduleInfo> scheduleInfos;

    @BindView(R.id.grid)
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //初始化数据库
        ScheduleData.initDB(getApplicationContext(), "Schedule.db");
        //检查数据库是否有数据
        EventBus.getDefault().post(new FinishEvent(true));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onFinishEvent(FinishEvent event) throws InterruptedException {
        LoadingDialogHelper.add(this);
        Thread.sleep(500);
        scheduleInfos =  ScheduleData.search();
        if (scheduleInfos.isEmpty()) {
            Schedule.getSchedule();
        } else {
            addSchedule();
            LoadingDialogHelper.remove(this);
        }
    }

    //接收获取数据的广播 并将课表存到本地数据库
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetSchedule(ScheduleEvent event) {
        ScheduleData.insert(event.getDay(), event.getCourse(), event.getTeacher(),
                event.getWeek(), event.getTime(), event.getClassroom());
    }

    //接收课表处理完毕的广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScheduleFinish(com.lepetit.eventmessage.FinishEvent event) {
        scheduleInfos = ScheduleData.search();
        addSchedule();
        LoadingDialogHelper.remove(this);
    }

    //打印课表
    private void addSchedule() {
        for (ScheduleInfo info : scheduleInfos) {
            SetSchedule setSchedule1 = new SetSchedule.Builder(this, gridLayout)
                    .course(info.getCourse())
                    .teacher(info.getTeacher())
                    .week(info.getWeek())
                    .time(info.getTime())
                    .day(info.getDay())
                    .classroom(info.getClassroom())
                    .build();
            setSchedule1.addToScreen();
        }
    }
}
