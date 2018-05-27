package com.lepetit.grade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.eventmessage.GradeEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.gradehelper.GetGradeInfo;
import com.lepetit.greendaohelper.GradeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.messagehelper.ConnectEvent;
import com.lepetit.messagehelper.FinishEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GradeFragment extends BackHandleFragment {

    private List<GradeInfo> list;

    @BindView(R.id.grade_list_view)
    RecyclerView recyclerView;
    @BindView(R.id.grade_list)
    Spinner spinner;
    @BindView(R.id.grade_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grade_fragment, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        setSpinner();
        setSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void getData() {
        year = spinner.getSelectedItem().toString();
        getGrade(REFRESH);
    }

    private void setSwipeRefreshLayout() {
        super.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private void getGrade(int method) {
        this.method = method;
        if (MainActivity.isLogin) {
            GetGradeInfo.get(year);
        }
        else {
            mainActivity.doSomeCheck();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.getLoginState() == 1) {
            GetGradeInfo.get(year);
        }
        else {
            if (method == SELECT) {
                setRecyclerView();
            }
            else {
                removeRefreshSign(swipeRefreshLayout);
            }
            setToast(LOGIN_ERROR);
        }
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
                if (GreenDaoUnit.isGradeEmpty()) {
                    getGrade(SELECT);
                }
                else {
                    setRecyclerView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConnectEvent(ConnectEvent event) {
        if (event.isSuccessful()) {
            GreenDaoUnit.clearGrade();
        }
        else {
            setToast(CONNECT_ERROR);
            setRecyclerView();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGradeEvent(GradeEvent event) {
        GreenDaoUnit.insertGrade(event.getCourse(), event.getScore(), event.getCredit());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetGradeFinish(FinishEvent event) {
        setRecyclerView();
    }

    private void setRecyclerView() {
        list = GreenDaoUnit.getGrade();
        GradeAdapter adapter = new GradeAdapter(list);
        super.setRecyclerView(recyclerView, adapter, swipeRefreshLayout);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
