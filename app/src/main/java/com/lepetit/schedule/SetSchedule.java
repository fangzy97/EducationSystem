package com.lepetit.schedule;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.widget.TextView;

import com.lepetit.leapplication.R;

public class SetSchedule {
    private Context context;
    private GridLayout gridLayout;

    public SetSchedule(Context context, GridLayout gridLayout) {
        this.context = context;
        this.gridLayout = gridLayout;
    }

    public void addToScreen(String course, String classroom, int rowStart, int rowSize, int columnStart) {
        _addToScreen(course, classroom, rowStart, rowSize, columnStart);
    }

    private void _addToScreen(String course, String classroom, int rowStart, int rowSize, int columnStart) {
        String text = course + "\n" + classroom;
        TextView textView =  setTextView(text);
        GridLayout.LayoutParams layoutParams = setLayoutParams(rowStart, rowSize, columnStart);
        gridLayout.addView(textView, layoutParams);
    }

    private TextView setTextView(String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundColor(context.getColor(R.color.colorPrimary));
        textView.setTextColor(Color.WHITE);
        textView.setPadding(5, 5, 5, 0);
        textView.setTextSize(12);
        return textView;
    }

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
