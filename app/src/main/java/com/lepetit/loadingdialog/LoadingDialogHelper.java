package com.lepetit.loadingdialog;

import android.app.Activity;

public class LoadingDialogHelper {
    private LoadingDialog dialog;
    private static LoadingDialogHelper instance;

    private LoadingDialogHelper() {
        dialog = new LoadingDialog();
        LoadingDialog.isAdd = false;
    }

    private static LoadingDialogHelper getInstance() {
        if (instance == null) {
            instance = new LoadingDialogHelper();
        }
        return instance;
    }

    private void _add(Activity activity) {
        if (!LoadingDialog.isAdd) {
            activity.getFragmentManager().beginTransaction().add(dialog, "Loading").commit();
            LoadingDialog.isAdd = true;
        }
    }

    private void _remove(Activity activity) {
        if (LoadingDialog.isAdd) {
            activity.getFragmentManager().beginTransaction().remove(dialog).commit();
            LoadingDialog.isAdd = false;
        }
    }

    public static void add(Activity activity) {
        getInstance()._add(activity);
    }

    public static void remove(Activity activity) {
        getInstance()._remove(activity);
    }
}
