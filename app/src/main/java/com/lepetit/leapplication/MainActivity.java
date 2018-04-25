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
    }

    @OnClick(R.id.scheduleButton)
    void writeSchedule() {
        GreenDaoUnit.insert("Math", "Jack", "1-8", "1-2", "1-302");
    }
}
