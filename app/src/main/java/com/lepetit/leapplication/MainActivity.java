package com.lepetit.leapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.updatemodule.API;
import com.example.updatemodule.Tools;
import com.lepetit.baseactivity.BaseActivity;
import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.basefragment.BackHandleInterface;
import com.lepetit.eventmessage.GetWeekEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.exam.ExamFragment;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.grade.GradeFragment;
import com.lepetit.greendaohelper.GradeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.login.LoginActivity;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.StoreInfo;
import com.lepetit.schedule.ScheduleFragment;
import com.lepetit.schedulehelper.GetWeekInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BackHandleInterface {
    private BackHandleFragment backHandleFragment;

    public static boolean isHaveUpdate;
    public static String downloadUrl;

    public static boolean isLogin;

	@BindView(R.id.tab_layout)
	public TabLayout tabLayout;
	@BindView(R.id.view_page)
	public ViewPager viewPager;
	@BindView(R.id.statusBar)
    public View statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
        Tools.getLatestVersion();
        //初始化toolBar
		setViewPager();
        isLogin = false;
        //检查SharedPreference是否为空，若为空则调用登录界面，否则直接用对应的用户名和密码登录
        setStatusBar();
        doSomeCheck();
    }

    private void setStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        int statusBarHeight = getStatusHeight();
        statusBar.setMinimumHeight(statusBarHeight);
    }

    private int getStatusHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelOffset(resourceId);
        }
        return -1;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
    }

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        int state = event.getLoginState();
        if (state == 1) {
            isLogin = true;
			GetWeekInfo.get();
        }
        else {
            getToast("暂时无法连接到教务处");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetWeekEvent(GetWeekEvent event) {
		String startWeek = event.getStartWeek();
		StoreInfo.storeStartWeek(startWeek);
        System.out.println("startWeek = " + startWeek);
    }

    private void setViewPager() {
    	List<Fragment> list = new ArrayList<>();
    	list.add(new MainFragment());
		list.add(new ScheduleFragment());
    	list.add(new ExamFragment());
    	list.add(new GradeFragment());

    	viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), list));
    	viewPager.setOffscreenPageLimit(3);
    	tabLayout.setupWithViewPager(viewPager);
	}

    public void clearDatabase() {
		List<String> list = GetTimeInfo.getTimeList();
		for (String time : list) {
			GreenDaoUnit.initialize(getApplicationContext(), time);
			if (GreenDaoUnit.isInitialize()) {
				GreenDaoUnit.clearAll();
			}
		}
		GreenDaoUnit.initialize(getApplicationContext(), "Grade");
		if (GreenDaoUnit.isInitialize()) {
			GreenDaoUnit.clearAll();
		}
	}

    public void doSomeCheck() {
        userName = getUserName();
        password = getPassword();
        if (isInfoEmpty()) {
            goToLoginActivity();
        }
        else {
            LoginPart.getLt();
        }
    }

    public void goToLoginActivity() {
        int LOGIN_REQUEST = 0;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            isLogin = true;
            setViewPager();
        }
        else {
            finish();
        }
    }

    @Override
    public void setSelectedFragment(BackHandleFragment selectedFragment) {
        this.backHandleFragment = selectedFragment;
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetVersionEvent(API event) {
        String localVersion = Tools.getLocalVersion(this);
        if (event.getVersion().equals(localVersion)) {
            this.runOnUiThread(() -> {
                Toast.makeText(this, "Application is latest", Toast.LENGTH_SHORT).show();
                isHaveUpdate = false;
            });
        }
        else {
            this.runOnUiThread(() -> {
                Toast.makeText(this, "New version is ready", Toast.LENGTH_SHORT).show();
                downloadUrl = event.getApk();
                isHaveUpdate = true;
            });
        }
    }
}
