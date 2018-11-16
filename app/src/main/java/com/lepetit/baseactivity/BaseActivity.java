package com.lepetit.baseactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lepetit.eventmessage.GetLtEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.StoreInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public abstract class BaseActivity extends AppCompatActivity {

    protected String userName;
    protected String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        StoreInfo.setPreferences(getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetLtEvent(GetLtEvent event) {
        if (event.isSuccessful()) {
            LoginPart.postData(userName, password);
        }
        else {
            getToast("网络好像发生了点问题");
        }
    }

    public abstract void onLoginEvent(LoginEvent event);

    protected String getUserName() {
        return StoreInfo.getInfo("UserName");
    }

    protected String getPassword() {
        return StoreInfo.getInfo("Password");
    }

    protected void getToast(String message) {
        BaseActivity.this.runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    protected boolean isInfoEmpty() {
        return userName.equals("") || password.equals("");
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(Environment.getExternalStorageDirectory() + getPackageName());
                        if (!file.exists()) {
                            System.out.println(file.mkdir());
                        }
                    }
                }
        }
    }
}
