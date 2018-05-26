package com.lepetit.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
;
import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.eventmessage.ScheduleEvent;
import com.lepetit.finalcollection.FinalCollection;
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
                LoadingDialogHelper.add(getActivity());
                year = spinner.getSelectedItem().toString();
                GreenDaoUnit.initialize(getContext(), year);
                if (GreenDaoUnit.isScheduleEmpty()) {
                    getSchedule(FinalCollection.SELECT);
                }
                else {
                    addSchedule();
                    LoadingDialogHelper.remove(getActivity());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Thread(() -> {
                year = spinner.getSelectedItem().toString();
                getSchedule(FinalCollection.REFRESH);
            }).start();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            if (method == FinalCollection.SELECT) {
                addSchedule();
            }
            else {
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                });
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
        GreenDaoUnit.insertSchedule(event.getDay(), event.getCourse(), event.getTeacher(),
                event.getWeek(), event.getTime(), event.getClassroom());
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
        LoadingDialogHelper.remove(getActivity());
        getActivity().runOnUiThread(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initialize() {
        for (int i = 21; i < gridLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
            getActivity().runOnUiThread(linearLayout::removeAllViews);
        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
