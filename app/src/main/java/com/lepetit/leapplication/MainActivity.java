package com.lepetit.leapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.greendaohelper.ScheduleInfo;
import com.lepetit.loginactivity.StoreInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final int LOGIN_REQUEST = 0;

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化数据库
        GreenDaoUnit.initialize(getApplicationContext());
        //转到登录界面
        goToLoginActivity();
        //初始化SharedPreference
        StoreInfo.setPreferences(getApplicationContext());
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    //储存用户名和密码
    private void storeInfo(Intent intent) {
        String name = intent.getStringExtra("userName");
        String pass = intent.getStringExtra("password");
        StoreInfo.storeInfo(name, pass);
    }

    //若登录成功，则储存用户名和密码
    //若按了返回键，结束MainActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            storeInfo(data);
        }
        else {
            finish();
        }
    }

    @OnClick(R.id.scheduleButton)
    void writeSchedule() {
        GreenDaoUnit.insert("Math", "Jack", "1-8", "1-2", "1-302");

    }
}
