package com.lepetit.loadingdialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lepetit.leapplication.R;

public class LoadingDialog extends DialogFragment {

    static boolean isAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog, container, false);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        isAdd = false;
    }
}
