package com.lepetit.leapplication;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.lepetit.eventmessage.GetLtEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.login.LoginActivity;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.StoreInfo;
import com.lepetit.schedule.ScheduleActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class MainActivity extends AppCompatActivity {
    private final int LOGIN_REQUEST = 0;

    private String user;
    private String pass;

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化SharedPreference
        StoreInfo.setPreferences(getApplicationContext());
        setActionBat();
        setNavigationView();
        //检查SharedPreference是否为空，若为空则调用登录界面，否则直接用对应的用户名和密码登录
        doSomeCheck();
    }

    private void setNavigationView() {
        navigationView.setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.schedule:
                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    StoreInfo.clearInfo();
                    goToLoginActivity();
                    break;
                default:
            }
            return true;
        });
    }

    private void setActionBat() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void doSomeCheck() {
        String userName = StoreInfo.getInfo("UserName");
        String password = StoreInfo.getInfo("Password");
        if (isInfoEmpty(userName, password)) {
            goToLoginActivity();
        }
        else {
            EventBus.getDefault().register(this);
            user = userName;
            pass = password;
            LoginPart.getLt();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetLtEvent(GetLtEvent event) {
        if (event.isSuccessful()) {
            LoginPart.postData(user, pass);
        }
        else {
            getToast("网络好像发生了点问题");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        int state = event.getLoginState();
        if (state == 1) {
            EventBus.getDefault().unregister(this);
        }
        else {
            getToast("暂时无法连接到教务处");
        }
    }

    private void getToast(String message) {
        MainActivity.this.runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isInfoEmpty(String userName, String password) {
        return userName.equals("") || password.equals("");
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    private boolean isRequestOK(int resultCode) {
        return resultCode == RESULT_OK;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isRequestOK(resultCode)) {
            user = data.getStringExtra("UserName");
            pass = data.getStringExtra("Password");
            StoreInfo.storeInfo(user, pass);
        }
        else {
            finish();
        }
    }
}
