package com.lepetit.basefragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lepetit.finalcollection.FinalCollection;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;

import org.greenrobot.eventbus.EventBus;

public abstract class BackHandleFragment extends Fragment {
    protected BackHandleInterface backHandleInterface;
    protected MainActivity mainActivity;
    protected String year;
    protected int method;
    protected static final String LOGIN_ERROR = "暂时无法登录，请检查网络设置";
    protected static final String CONNECT_ERROR = "暂时无法连接到教务处，请检查网络设置";

    public abstract boolean onBackPressed();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        if (!(getActivity() instanceof BackHandleInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
        else {
            backHandleInterface = (BackHandleInterface)getActivity();
            backHandleInterface.setSelectedFragment(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandleInterface.setSelectedFragment(this);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void setToast(String message) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    protected void setRecyclerView(
            RecyclerView recyclerView, RecyclerView.Adapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        getActivity().runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        });
        LoadingDialogHelper.remove(getActivity());
    }

    protected void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Thread(this::getData).start();
        });
    }

    protected abstract void getData();
}
