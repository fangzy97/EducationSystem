package com.lepetit.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.lepetit.eventmessage.GetYearEvent;
import com.lepetit.eventmessage.ScheduleEvent;
import com.lepetit.greendaohelper.ScheduleData;
import com.lepetit.greendaohelper.ScheduleInfo;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialog;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.schedulehelper.Schedule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleFragment extends Fragment {

    private List<ScheduleInfo> scheduleInfos;

    @BindView(R.id.grid)
    GridLayout gridLayout;
    @BindView(R.id.spinner)
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setSpinner();
        ScheduleData.initDB(getContext(), spinner.getSelectedItem().toString());
        return view;
    }

    private ArrayAdapter<String> setAdapter() {
        List<String> list = setList();
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
    }

    private List<String> setList() {
        List<String> list = new ArrayList<>();
        GetTime getTime = new GetTime();
        int year = getTime.getYear();
        int month = getTime.getMonth();

        for (int i = 2014; i < year; i++) {
            String string1 = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-1";
            String string2 = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-2";
            list.add(string1);
            list.add(string2);
        }

        if (month > 7) {
            String string = String.valueOf(year) + "-" + String.valueOf(year + 1) + "-1";
            list.add(string);
        }
        return list;
    }

    private void setSpinner() {
        ArrayAdapter<String> spinnerAdapter = setAdapter();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinner.getCount() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadingDialogHelper.add(getActivity());
                initialize();
                String year = spinner.getSelectedItem().toString();
                ScheduleData.initDB(getContext(), year);
                if (isDatabaseEmpty()) {
                    Schedule.getChosenSchedule(year);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        LoadingDialogHelper.remove(getActivity());
    }

    private boolean isDatabaseEmpty() {
        scheduleInfos = ScheduleData.search();
        return scheduleInfos.isEmpty();
    }

    //打印课表
    private void addSchedule() {
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
    }

    private void initialize() {
        for (int i = 21; i < gridLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
            getActivity().runOnUiThread(linearLayout::removeAllViews);
        }
    }
}
