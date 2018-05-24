package com.lepetit.leapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lepetit.basefragment.BackHandleFragment;
import com.lepetit.exam.ExamFragment;
import com.lepetit.gettimehelper.GetTimeInfo;
import com.lepetit.greendaohelper.ExamInfo;
import com.lepetit.greendaohelper.GreenDaoUnit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends BackHandleFragment {

    private List<String> list;

    @BindView(R.id.main_list)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        GreenDaoUnit.initialize(getContext(), GetTimeInfo.getSimpleSTime());
        initialize();
        return view;
    }

    @OnClick(R.id.exam_card)
    void onCardViewClick() {
        goToExamFragment();
    }

    private void goToExamFragment() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setExamChecked();
        activity.changeFragment(new ExamFragment(), R.string.Exam);
    }

    private void initialize() {
        list = new ArrayList<>();
        setList();
        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        MainAdapter adapter = new MainAdapter(list, (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void setList() {
        String string = "近一个月没有考试";
        if (GreenDaoUnit.isExamEmpty()) {
            list.add(string);
        }
        else {
            List<ExamInfo> temp = GreenDaoUnit.getExam();
            String dateNow = GetTimeInfo.getDate();
            for (ExamInfo info : temp) {
                String course = info.getCourse();
                String time = info.getTime();
                int loc = time.indexOf("日");
                String dateExam = time.substring(0, loc);
                System.out.println(dateExam);
                System.out.println(dateNow);
                try {
                    int pastDay = getPastDay(dateNow, dateExam);
                    if (pastDay > -1) {
                        list.add(dateExam + "   " + course);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (list.isEmpty()) {
                list.add(string);
            }
        }
    }



    private int getPastDay(String dateNow, String dateExam) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(dateNow);
        Date date2 = format.parse(dateExam);
        System.out.println(date1);
        System.out.println(date2);
        int temp = (int)(date2.getTime() - date1.getTime());
        if (temp < 0) {
            return -1;
        }
        return temp / (1000*3600*24);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
