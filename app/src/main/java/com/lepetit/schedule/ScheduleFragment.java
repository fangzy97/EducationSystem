package com.lepetit.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
;
import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.eventmessage.ScheduleEvent;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.greendaohelper.ScheduleInfo;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.messagehelper.ConnectEvent;
import com.lepetit.messagehelper.FinishEvent;
import com.lepetit.schedulehelper.Schedule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleFragment extends BackHandleFragment {

    private List<ScheduleInfo> scheduleInfos;

    @BindView(R.id.grid)
    GridLayout gridLayout;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.schedule_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setSpinner();
        scheduleInfos = new ArrayList<>();
        GreenDaoUnit.initialize(getContext(), spinner.getSelectedItem().toString());
        setSwipeRefreshLayout();
        return view;
    }

    private ArrayAdapter<String> setAdapter() {
        List<String> list = GetTimeInfo.getTimeList();
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
    }

    private void setSpinner() {
        ArrayAdapter<String> spinnerAdapter = setAdapter();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinner.getCount() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadingDialogHelper.add((AppCompatActivity) getActivity());
                year = spinner.getSelectedItem().toString();
                GreenDaoUnit.initialize(getContext(), year);
                if (GreenDaoUnit.isScheduleEmpty()) {
                    getSchedule(SELECT);
                }
                else {
                    addSchedule();
                    LoadingDialogHelper.remove((MainActivity) getActivity());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSwipeRefreshLayout() {
        super.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void getData() {
        year = spinner.getSelectedItem().toString();
        getSchedule(REFRESH);
    }

    private void getSchedule(int method) {
        this.method = method;
        if (MainActivity.isLogin) {
            Schedule.getChosenSchedule(year);
        }
        else {
            mainActivity.doSomeCheck();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.getLoginState() == 1) {
            Schedule.getChosenSchedule(year);
        }
        else {
            if (method == SELECT) {
                addSchedule();
            }
            else {
                removeRefreshSign(swipeRefreshLayout);
            }
            setToast(LOGIN_ERROR);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConnectEvent(ConnectEvent event) {
        if (event.isSuccessful()) {
            GreenDaoUnit.clearSchedule();
        }
        else {
            setToast(CONNECT_ERROR);
            addSchedule();
        }
    }

    //接收获取数据的广播 并将课表存到本地数据库
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetSchedule(ScheduleEvent event) {
    	boolean flag = false;

        String day = event.getDay();
        String course = event.getCourse();
        String teacher = event.getTeacher();
        String week = event.getWeek();
        String time = event.getTime();
        String classroom = event.getClassroom();

		List<ScheduleInfo> list = GreenDaoUnit.querySchedule(course);
		for (ScheduleInfo info : list) {
			flag = day.equals(info.getDay()) && teacher.equals(info.getTeacher()) && week.equals(info.getWeek())
					&& time.equals(info.getTime()) && classroom.equals(info.getClassroom());
			if (flag) {
				break;
			}
		}
		if (!flag) {
			GreenDaoUnit.insertSchedule(day, course, teacher, week, time, classroom);
		}
    }

    //接收课表处理完毕的广播
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getScheduleFinish(FinishEvent event) {
        addSchedule();
    }

    //打印课表
    private void addSchedule() {
        initialize();
        scheduleInfos = GreenDaoUnit.getSchedule();
        for (ScheduleInfo info : scheduleInfos) {
            SetScheduleInfo setScheduleInfo1 = new SetScheduleInfo.Builder(getActivity(), gridLayout)
                    .course(info.getCourse())
                    .teacher(info.getTeacher())
                    .week(info.getWeek())
                    .time(info.getTime())
                    .day(info.getDay())
                    .classroom(info.getClassroom())
                    .build();
            setScheduleInfo1.addToScreen();
        }
        LoadingDialogHelper.remove((AppCompatActivity) getActivity());
        removeRefreshSign(swipeRefreshLayout);
    }

    private void initialize() {
        for (int i = 21; i < gridLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
            mainActivity.runOnUiThread(linearLayout::removeAllViews);
        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mainActivity.setItemUnchecked(R.id.schedule);
	}
}
