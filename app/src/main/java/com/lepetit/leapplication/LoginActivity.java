package com.lepetit.leapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.eventmessage.GetLtEvent;
import com.lepetit.loginactivity.StoreInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

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
        //绑定ButterKnife
        ButterKnife.bind(this);
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    //点击按钮开始登录
    @OnClick(R.id.loginButton)
    void onLogin() {
        loginPart = new LoginPart();
        loginPart.getLt();      //获取隐藏值
    }

    //接收获取隐藏值成功的事件
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetLtEvent(GetLtEvent event) {
        if (event.isSuccessful()) {
            loginPart.postData(userName.getText().toString(), password.getText().toString());
        }
        else {
            getToast("网络好像发生了点问题");
        }
    }

    //接受登录成功的事件
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.isLoginSuccessful()) {
            sendInfoBack();
            finish();
        }
        else {
            getToast("用户名或密码错误");
        }
    }

    //将用户名和密码传回MainActivity以储存
    private void sendInfoBack() {
        Intent intent = new Intent();
        intent.putExtra("userName", userName.getText().toString());
        intent.putExtra("password", password.getText().toString());
        setResult(RESULT_OK, intent);
    }

    //生成Toast
    private void getToast(String message) {
        LoginActivity.this.runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        });
    }
}
