package com.lepetit.loadingdialog;

import android.app.Activity;

public class LoadingDialogHelper {
    private LoadingDialog dialog;
    private static LoadingDialogHelper instance;

    private LoadingDialogHelper() {
        dialog = new LoadingDialog();
        dialog.setCancelable(false);
    }

    private static LoadingDialogHelper getInstance() {
        if (instance == null) {
            instance = new LoadingDialogHelper();
        }
        return instance;
    }

    private void _add(Activity activity) {
        activity.getFragmentManager().beginTransaction().add(dialog, "Loading").commit();
    }

    private void _remove(Activity activity) {
        activity.getFragmentManager().beginTransaction().remove(dialog).commit();
    }

    public static void add(Activity activity) {
        getInstance()._add(activity);
    }

    public static void remove(Activity activity) {
        getInstance()._remove(activity);
    }
}
