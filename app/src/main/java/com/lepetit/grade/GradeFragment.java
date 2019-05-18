package com.lepetit.grade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.eventmessage.GradeEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.gradehelper.GetGradeInfo;
import com.lepetit.greendaohelper.GradeInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;
import com.lepetit.leapplication.MainActivity;
import com.lepetit.leapplication.R;
import com.lepetit.loadingdialog.LoadingDialogHelper;
import com.lepetit.messagehelper.ConnectEvent;
import com.lepetit.messagehelper.FinishEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GradeFragment extends BackHandleFragment {

    private List<GradeInfo> list;
    private double score;
    private double credit;

	@BindView(R.id.grade_list_view)
    RecyclerView recyclerView;
    @BindView(R.id.grade_list)
    Spinner spinner;
    @BindView(R.id.grade_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.avg_score)
	TextView avg_score_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grade_fragment, container, false);
        ButterKnife.bind(this, view);

		ArrayAdapter<String> spinnerAdapter = setAdapter();
		spinner.setAdapter(spinnerAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

	private void init() {
		list = new ArrayList<>();
		GreenDaoUnit.initialize(getContext(), "Grade");
		setSpinner();
		setSwipeRefreshLayout();
		if (!GreenDaoUnit.isGradeEmpty()) {
			setAvgView();
		}
	}

	@Override
    protected void getData() {
        year = spinner.getSelectedItem().toString();
        getGrade(REFRESH);
    }

    @Override
    protected void loadData() {
		System.out.println("Grade Load Data");
        EventBus.getDefault().register(this);
		init();
    }

    @Override
    protected void destroyData() {
		EventBus.getDefault().unregister(this);
    }

    private void initialize() {
    	score = 0;
    	credit = 0;
	}

    private void setSwipeRefreshLayout() {
        super.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private void getGrade(int method) {
        this.method = method;
        if (MainActivity.isLogin) {
            GetGradeInfo.get();
        }
        else {
            mainActivity.doSomeCheck();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginEvent(LoginEvent event) {
        if (event.getLoginState() == 1) {
            GetGradeInfo.get();
        }
        else {
            if (method == SELECT) {
                setRecyclerView();
            }
            else {
                removeRefreshSign(swipeRefreshLayout);
            }
            setToast(LOGIN_ERROR);
        }
    }

    private ArrayAdapter<String> setAdapter() {
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, timeList);
    }

    private void setSpinner() {
		spinner.setSelection(getSelectYear(spinner));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinnerSelected() {
		LoadingDialogHelper.add(mainActivity);
		year = spinner.getSelectedItem().toString();
		if (GreenDaoUnit.isGradeEmpty()) {
			getGrade(SELECT);
		}
		else {
			setRecyclerView();
		}
	}

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConnectEvent(ConnectEvent event) {
        if (event.isSuccessful()) {
            GreenDaoUnit.clearGrade();
        }
        else {
            setToast(CONNECT_ERROR);
            setRecyclerView();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGradeEvent(GradeEvent event) {
        GreenDaoUnit.insertGrade(event.getYear(), event.getCourse(), event.getScore(), event.getCredit(), event.getAnalyze());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetGradeFinish(FinishEvent event) {
        setRecyclerView();
		setAvgView();
    }

    private void setAvgView() {
		list = GreenDaoUnit.getGrade();
		getAvgGradeInfoFromDB();

		double avg_score = score / credit;
		NumberFormat format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(3);
		String result = "当前加权平均分：" + format.format(avg_score);
		avg_score_view.setText(result);
	}

    private void setRecyclerView() {
        list = GreenDaoUnit.queryGrade(year);
        GradeAdapter adapter = new GradeAdapter(list, mainActivity);
        super.setRecyclerView(recyclerView, adapter, swipeRefreshLayout);
    }

    private void getAvgGradeInfoFromDB() {
    	initialize();
    	for (GradeInfo info : list) {
    		score += Double.valueOf(info.getScore()) * Double.valueOf(info.getCredit());
    		credit += Double.valueOf(info.getCredit());
		}
	}

    @Override
    public boolean onBackPressed() {
        return true;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
