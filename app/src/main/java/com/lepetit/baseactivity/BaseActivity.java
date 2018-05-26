package com.lepetit.baseactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lepetit.eventmessage.GetLtEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.StoreInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    protected String userName;
    protected String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoreInfo.setPreferences(getApplicationContext());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}
