package com.lepetit.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.basefragment.BackHandleInterface;
import com.lepetit.eventmessage.ExamEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.examhelper.GetExamInfo;
import com.lepetit.finalcollection.FinalCollection;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.ExamInfo;
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

public class ExamFragment extends BackHandleFragment {
    private BackHandleInterface backHandleInterface;
    private List<ExamInfo> examList;

    @BindView(R.id.exam_list)
    RecyclerView recyclerView;
    @BindView(R.id.exam_fresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_fragment, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        LoadingDialogHelper.add(getActivity());
        examList = new ArrayList<>();
        GreenDaoUnit.initialize(getContext(), GetTimeInfo.getSimpleSTime());
        getExamInfo();
        setSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Thread(() -> {
                getExam(FinalCollection.REFRESH);
            }).start();
        });
    }

    private void getExamInfo() {
        if (GreenDaoUnit.isExamEmpty()) {
            getExam(FinalCollection.SELECT);
        }
        else {
            setRecyclerView();
        }
    }

    private void getExam(int method) {
        this.method = method;
        if (MainActivity.isLogin) {
            GetExamInfo.get();
        }
        else {
            mainActivity.doSomeCheck();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.getLoginState() == 1) {
            GetExamInfo.get();
        }
        else {
            if (method == FinalCollection.SELECT) {
                setRecyclerView();
            }
            else {
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
            setToast(LOGIN_ERROR);
        }
    }

    private void setRecyclerView() {
        examList = GreenDaoUnit.getExam();
        getActivity().runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            ExamAdapter adapter = new ExamAdapter(examList);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        });
        LoadingDialogHelper.remove(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConnectEvent(ConnectEvent event) {
        if (event.isSuccessful()) {
            GreenDaoUnit.clearExam();
        }
        else {
            setToast(CONNECT_ERROR);
            setRecyclerView();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onExamEvent(ExamEvent event) {
        GreenDaoUnit.insertExam(event.getCourse(), event.getTime(), event.getClassroom(), event.getSeat());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onFinish(FinishEvent event) {
        setRecyclerView();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
