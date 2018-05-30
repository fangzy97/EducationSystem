package com.lepetit.loadingdialog;

import android.support.v7.app.AppCompatActivity;

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

    private void _add(AppCompatActivity activity) {
        if (!LoadingDialog.isAdd) {
            activity.getSupportFragmentManager().beginTransaction().add(dialog, "Loading").commit();
            LoadingDialog.isAdd = true;
        }
    }

    private void _remove(AppCompatActivity activity) {
        if (LoadingDialog.isAdd) {
            activity.getSupportFragmentManager().beginTransaction().remove(dialog).commit();
            LoadingDialog.isAdd = false;
        }
    }

    public static void add(AppCompatActivity activity) {
        getInstance()._add(activity);
    }

    public static void remove(AppCompatActivity activity) {
        getInstance()._remove(activity);
    }
}
