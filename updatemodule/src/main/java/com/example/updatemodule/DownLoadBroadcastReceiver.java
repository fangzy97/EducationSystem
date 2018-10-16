package com.example.updatemodule;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class DownLoadBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long completedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadAPK", 0);
        long id = sharedPreferences.getLong("ID", 0);

        if (id == completedId) {
            Tools.install(context);
        }
    }
}
