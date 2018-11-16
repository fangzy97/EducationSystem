package com.example.updatemodule;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tools {
    private static String url = "http://39.106.170.222/update/api/update";

    public static String getLocalVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getLatestVersion() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                API api = gson.fromJson(json, API.class);
                EventBus.getDefault().post(api);
            }
        });
    }

    public static void downloadAPK(Context context, String url) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName(), "EducationSystem.apk");
        if (file.exists()) {
            boolean deleteSuccess = file.delete();
            if (deleteSuccess) {
                System.out.println("Delete success");
            }
            else {
                System.out.println("Delete error");
            }
        }

        Uri uri = Uri.parse(url);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request requestAPK = new DownloadManager.Request(uri);
        requestAPK.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        requestAPK.setDestinationInExternalPublicDir(context.getPackageName(), "EducationSystem.apk");
        requestAPK.setVisibleInDownloadsUi(true);
        requestAPK.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        requestAPK.allowScanningByMediaScanner();
        requestAPK.setTitle("EducationSystem Update");
        requestAPK.setDescription("EducationSystem Update");

        long downloadID = manager.enqueue(requestAPK);
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadAPK", 0);
        sharedPreferences.edit().putLong("ID", downloadID).apply();
    }

    public static void install(Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName(), "EducationSystem.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean installAllowed = context.getPackageManager().canRequestPackageInstalls();
            if (!installAllowed) {
                Intent getInstallPermission = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()));
                getInstallPermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(getInstallPermission);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(context, "com.lepetit.leapplication.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
