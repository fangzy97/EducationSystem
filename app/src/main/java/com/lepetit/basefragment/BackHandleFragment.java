package com.lepetit.basefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.loadingdialog.LoadingDialogHelper;

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

    protected void setToast(String message) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}
