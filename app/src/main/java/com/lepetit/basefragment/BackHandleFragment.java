package com.lepetit.basefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.loadingdialog.LoadingDialogHelper;

public abstract class BackHandleFragment extends Fragment {
    protected BackHandleInterface backHandleInterface;
    protected MainActivity mainActivity;
    protected String year;
    protected int method;
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
}
