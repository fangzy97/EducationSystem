package com.lepetit.login;

import android.os.Bundle;
import android.widget.EditText;

import com.lepetit.baseactivity.BaseActivity;
import com.lepetit.eventmessage.GetWeekEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.loginactivity.LoginPart;
import com.lepetit.loginactivity.StoreInfo;
import com.lepetit.schedulehelper.GetWeekInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    //错误三次后会产生验证码

    @BindView(R.id.userName)
    EditText userNameText;
    @BindView(R.id.password)
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定ButterKnife
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    //点击按钮开始登录
    @OnClick(R.id.loginButton)
    void onLogin() {
        userName = userNameText.getText().toString();
        password = passwordText.getText().toString();
        if (!isInfoEmpty()) {
            LoadingDialogHelper.add(this);
            LoginPart.getLt();      //获取隐藏值
        }
        else {
            getToast("用户名或密码不能为空");
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        int state = event.getLoginState();
        if (state == 1) {
            storeInfo();
			GetWeekInfo.get();
        }
        else if (state == 0){
            getToast("用户名或密码错误");
        }
        else {
            getToast("没有连接到校园网");
        }
        LoadingDialogHelper.remove(this);
    }

	@Subscribe(threadMode = ThreadMode.POSTING)
	public void onGetWeekEvent(GetWeekEvent event) {
		String startWeek = event.getStartWeek();
		String endWeek = event.getEndWeek();
		StoreInfo.storeStartAndEndTime(startWeek, endWeek);
		System.out.println("startWeek = " + startWeek);
        System.out.println("endWeek = " + endWeek);
		setResult(RESULT_OK);
		finish();
	}

    private void storeInfo() {
        StoreInfo.storeInfo(userName, password);
    }
}
