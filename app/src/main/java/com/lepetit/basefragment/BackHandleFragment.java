package com.lepetit.basefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BackHandleFragment extends Fragment {
    protected BackHandleInterface backHandleInterface;
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
