package com.lepetit.leapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.updatemodule.Tools;
import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.ExamInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.loginactivity.StoreInfo;
import com.lepetit.web.UrlCollection;
import com.lepetit.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends BackHandleFragment {

    private List<MainExamInfo> list;
    private String dateNow;

    @BindView(R.id.main_list)
    RecyclerView recyclerView;
    @BindView(R.id.main_month_and_day)
    TextView monthAndDay;
    @BindView(R.id.main_week)
    TextView weekText;
    @BindView(R.id.update)
    TextView update;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        GreenDaoUnit.initialize(getContext(), GetTimeInfo.getSimpleTime());
        initialize();
        setUpdate();
        return view;
    }

    @OnClick(R.id.edu_web)
    void onEduWebClick() {
        goToWebActivity(UrlCollection.JWC);
    }

    @OnClick(R.id.bit_online)
    void onBitOnlineClick() {
        goToWebActivity(UrlCollection.BIT_ONLINE);
    }

    @OnClick(R.id.logout)
	void onLogoutClick() {
        openAlertDialog();
	}

	private void openAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("注销")
                .setMessage("确定要注销吗？")
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    StoreInfo.clearInfo();
                    mainActivity.clearDatabase();
                    mainActivity.goToLoginActivity();
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    private void goToWebActivity(String url) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void initialize() {
        try {
			dateNow = GetTimeInfo.getDate();
			list = new ArrayList<>();
			setTextView();
			setList();
			setRecyclerView();
		} catch (Exception e) {
        	e.printStackTrace();
			Toast.makeText(getContext(), "404 Error", Toast.LENGTH_SHORT).show();
		}
    }

    private void setTextView() {
        int loc = dateNow.indexOf("-");
        String date = dateNow.substring(loc + 1);
        date = date.replace("-", "月");
        date += "日";
        monthAndDay.setText(date);

        String week = "星期" + intToString(GetTimeInfo.getWeek());
        weekText.setText(week);
    }

    private String intToString(int num) {
        switch (num) {
            case 1: return "日";
            case 2: return "一";
            case 3: return "二";
            case 4: return "三";
            case 5: return "四";
            case 6: return "五";
            case 7: return "六";
            default: return "";
        }
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        MainAdapter adapter = new MainAdapter(list, mainActivity);
        recyclerView.setAdapter(adapter);
    }

    private void setList() {
        if (!GreenDaoUnit.isExamEmpty()) {
            int examCount = 0;
            List<ExamInfo> temp = GreenDaoUnit.getExam();
            if (temp.isEmpty()) {
                list.add(new MainExamInfo("最近没有考试", "", ""));
            }
            else {
				for (ExamInfo info : temp) {
					String head = "剩余：";
					String course = info.getCourse();
					String time = info.getTime();
					int loc = time.indexOf("日");
					if (loc == -1) {
					    loc = time.indexOf(" ");
                    }
					String dateExam = time.substring(0, loc);

					int pastDay = GetTimeInfo.getPastDay(dateNow, dateExam);
					if (pastDay > -1) {
						head += String.valueOf(pastDay) + "天";
						list.add(new MainExamInfo(dateExam, course, head));
						examCount++;
					}
				}
				if (examCount == 0) {
                    list.add(new MainExamInfo("最近没有考试", "", ""));
                }
			}
        }
        else {
        	list.add(new MainExamInfo("最近没有考试", "", ""));
		}
    }

    private void setUpdate() {
        update.setOnClickListener((e) -> {
            Tools.getLatestVersion();
            if (MainActivity.isHaveUpdate) {
                // Toast.makeText(getActivity(), "发现新版本，开始下载", Toast.LENGTH_SHORT).show();
                mainActivity.openUpdateDialog();
            }
            else {
                Toast.makeText(mainActivity, "已经是最新版本！", Toast.LENGTH_SHORT).show();
            }
        });
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void getData() {}

	@Override
	protected void loadData() {

	}

	@Override
	protected void destroyData() {

	}
}
