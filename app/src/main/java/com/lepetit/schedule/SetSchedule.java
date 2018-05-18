package com.lepetit.schedule;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.widget.TextView;

import com.lepetit.leapplication.R;

public class SetSchedule {

    private GridLayout gridLayout;
    private Activity activity;

    public SetSchedule(Activity activity, GridLayout gridLayout) {
        this.activity = activity;
        this.gridLayout = gridLayout;
    }

    public void addToScreen(String course, String classroom, int rowStart, int rowSize, int columnStart) {
        _addToScreen(course, classroom, rowStart, rowSize, columnStart);
    }

    //将控件添加到gridLayout中
    private void _addToScreen(String course, String classroom, int rowStart, int rowSize, int columnStart) {
        String text = course + "\n" + classroom;
        TextView textView =  setTextView(text);
        GridLayout.LayoutParams layoutParams = setLayoutParams(rowStart, rowSize, columnStart);
        gridLayout.addView(textView, layoutParams);
    }

    //设置textView中的内容
    private TextView setTextView(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundColor(activity.getColor(R.color.colorPrimary));
        textView.setTextColor(Color.WHITE);
        textView.setPadding(5, 5, 5, 0);
        textView.setTextSize(12);
        return textView;
    }

    //设置控件在gridLayout中的参数
    private GridLayout.LayoutParams setLayoutParams(int rowStart, int rowSize, int columnStart) {
        GridLayout.Spec rowSpec = GridLayout.spec(rowStart, rowSize, 2f);
        GridLayout.Spec columnSpec = GridLayout.spec(columnStart, 1, 2f);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
        layoutParams.height = 0;
        layoutParams.width = 0;
        layoutParams.setMargins(5, 5, 5, 5);
        return layoutParams;
    }
}
