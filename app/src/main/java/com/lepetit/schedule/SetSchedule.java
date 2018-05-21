package com.lepetit.schedule;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lepetit.leapplication.R;

public class SetSchedule {
    private final Activity activity;
    private final GridLayout gridLayout;
    private final String course;
    private final String teacher;
    private final String week;
    private final String time;
    private final String day;
    private final String classroom;

    private SetSchedule(Builder builder) {
        this.activity = builder.activity;
        this.gridLayout = builder.gridLayout;
        this.course = builder.mCourse;
        this.teacher = builder.mTeacher;
        this.week = builder.mWeek;
        this.time = builder.mTime;
        this.day = builder.mDay;
        this.classroom = builder.mClassroom;
    }

    public void addToScreen() {
        _addToScreen();
    }

    private LinearLayout getLinearLayout() {
        int mDay = Integer.parseInt(day);
        int startTime = Integer.parseInt(time.substring(0, 2));
        int index = 20 + 5 * (mDay - 1) + dealStartTime(startTime);
        return (LinearLayout) gridLayout.getChildAt(index);
    }

    private int dealStartTime(int startTime) {
        switch (startTime) {
            case 1: return 1;
            case 3: return 2;
            case 6: return 3;
            case 8: return 4;
            case 11 : return 5;
            default: return 0;
        }
    }

    //将控件添加到gridLayout中
    private void _addToScreen() {
        LinearLayout linearLayout = getLinearLayout();
        String text = course + "\n" + classroom;
        TextView textView = setTextView(text);
        LinearLayout.LayoutParams layoutParams = setLayoutParams(linearLayout.getHeight());
        activity.runOnUiThread(() -> {
            linearLayout.addView(textView, layoutParams);
        });
    }

    //设置textView中的内容
    private TextView setTextView(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundColor(activity.getColor(R.color.colorPrimary));
        textView.setTextColor(Color.WHITE);
        textView.setPadding(5, 5, 5, 5);
        textView.setTextSize(12);
        textView.setOnClickListener((v) -> {
            ScheduleInfoFragment fragment = new ScheduleInfoFragment();
            Bundle bundle = setBundle();
            fragment.setArguments(bundle);
            activity.getFragmentManager().beginTransaction().add(fragment, "1").commit();
        });
        return textView;
    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("course", course);
        bundle.putString("teacher", teacher);
        bundle.putString("time", week + " " + time);
        bundle.putString("classroom", classroom);
        return bundle;
    }

    //设置控件在linearLayout中的参数
    private LinearLayout.LayoutParams setLayoutParams(int height) {
        int startTime = Integer.parseInt(time.substring(0, 2));
        int endTime = Integer.parseInt(time.substring(time.length() - 3, time.length() - 1));
        int mHeight = getHeight(startTime, endTime, height);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, mHeight);
        layoutParams.weight = 1;
        layoutParams.setMargins(2, 2, 2, 2);
        return layoutParams;
    }

    private int getHeight(int startTime, int endTime, int height) {
        if (isOnlyTwo(startTime)) {
            return height;
        }
        else {
            int size = endTime - startTime + 1;
            if (size == 2) {
                return height / 3 * 2;
            }
            else {
                return height;
            }
        }
    }

    private boolean isOnlyTwo(int startTime) {
        return startTime == 1 || startTime == 6;
    }

    public static class Builder {
        private final Activity activity;
        private final GridLayout gridLayout;

        private String mCourse;
        private String mTeacher;
        private String mWeek;
        private String mTime;
        private String mDay;
        private String mClassroom;

        Builder(Activity activity, GridLayout gridLayout) {
            this.activity = activity;
            this.gridLayout = gridLayout;
        }

        public Builder course(String value) {
            this.mCourse = value;
            return this;
        }

        public Builder teacher(String value) {
            this.mTeacher = value;
            return this;
        }

        public Builder week(String value) {
            this.mWeek = value;
            return this;
        }

        public Builder time(String value) {
            this.mTime = value;
            return this;
        }

        public Builder day(String value) {
            this.mDay = value;
            return this;
        }

        public Builder classroom(String value) {
            this.mClassroom = value;
            return this;
        }

        public SetSchedule build() {
            return new SetSchedule(this);
        }
    }
}
