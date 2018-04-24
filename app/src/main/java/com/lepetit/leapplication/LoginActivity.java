package com.lepetit.leapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lepetit.loginactivity.LoginEvent;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.GetLtEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    //用户名或密码输入错误后无法再次登录
    //错误三次后会产生验证码

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.password)
    EditText password;

    private LoginPart loginPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.loginButton)
    void onLogin() {
        loginPart = new LoginPart();
        loginPart.getLt();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetLtEvent(GetLtEvent event) {
        if (event.isSuccessful()) {
            loginPart.postData(userName.getText().toString(), password.getText().toString());
        }
        else {
            getToast("网络好像发生了点问题");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.isLoginSuccessful()) {
            storeInfo();
            finish();
        }
        else {
            getToast("用户名或密码错误");
        }
    }

    private void getToast(String message) {
        LoginActivity.this.runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        });
    }

    private void storeInfo() {
        SharedPreferences.Editor editor = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
        editor.putString("UserName", userName.getText().toString());
        editor.putString("Password", password.getText().toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
