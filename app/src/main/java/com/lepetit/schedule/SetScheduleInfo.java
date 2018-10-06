package com.lepetit.schedule;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lepetit.leapplication.R;

import java.util.ArrayList;
import java.util.List;

class SetScheduleInfo {
	private final Activity activity;
	private final GridLayout gridLayout;
	private final String course;
	private final String teacher;
	private final String week;
	private final String time;
	private final String day;
	private final String classroom;
	private final String lastWeek;

	private final int startTime;
	private final int endTime;
	private final int length;

	private SetScheduleInfo(Builder builder) {
		this.activity = builder.activity;
		this.gridLayout = builder.gridLayout;
		this.course = builder.mCourse;
		this.teacher = builder.mTeacher;
		this.week = builder.mWeek;
		this.time = builder.mTime;
		this.day = builder.mDay;
		this.classroom = builder.mClassroom;
		this.lastWeek = builder.mLastWeek;

		startTime = Integer.parseInt(time.substring(0, 2));
		endTime = Integer.parseInt(time.substring(time.length() - 3, time.length() - 1));
		length = getLength();
	}

	void addToScreen(String curWeek) {
		_addToScreen(curWeek);
	}

	private LinearLayout getLinearLayout() {
		int mDay = Integer.parseInt(day);
		int index = getIndex(mDay);
		return (LinearLayout) gridLayout.getChildAt(index);
	}

	private int getIndex(int day) {
		if (day == 6 || day == 7) {
			return 45 + 6 * (day - 6) + dealStartTime(day);
		}
		else {
			return 20 + 5 * (day - 1) + dealStartTime(day);
		}
	}

	private int dealStartTime(int day) {
		switch (startTime) {
			case 1:
			case 2:
				if (length == 2 || length == 1) {
					return 1;
				}
				else {
					return 5;
				}
			case 3:
				return 2;
			case 6:
				if (length == 2) {
					return 3;
				}
				else {
					return 6;
				}
			case 8:
				return 4;
			case 11 :
				return 5;
			default:
				return 0;
		}
	}

	//将控件添加到gridLayout中
	private void _addToScreen(String curWeek) {
		curWeek = getCurWeek(curWeek);
		if (curWeek.isEmpty() || isThisWeekHaveThisClass(curWeek)) {
			TextView textView = setTextView(course, classroom);
			GridLayout.LayoutParams layoutParams = setLayoutParams();
			activity.runOnUiThread(() -> {
				gridLayout.addView(textView, layoutParams);
			});
		}
	}

	private boolean isThisWeekHaveThisClass(String curWeek) {
		String temp = lastWeek;
		int index;
		String number;
		while (temp.length() > 0) {
			index = temp.indexOf(";");
			number = temp.substring(0, index);
			if (number.equals(curWeek)) {
				return true;
			}
			else {
				temp = temp.substring(index + 1);
			}
		}
		return false;
	}

	private String getCurWeek(String curWeek) {
		return curWeek.replaceAll("\\D", "");
	}

	//设置textView中的内容
	private TextView setTextView(String course, String classroom) {
	    String text = course + "\n" + classroom;
		TextView textView = new TextView(activity);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setBackgroundResource(R.drawable.corner_view);

        // 设置textView圆角以及背景
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(20f);

        String[] colors = activity.getResources().getStringArray(R.array.colors);
        if (isCourseExist(course)) {
        	activity.getResources().getStringArray(R.array.colors);

            int course_loc = ScheduleFragment.courses.indexOf(course) % colors.length;
            drawable.setColor(Color.parseColor(colors[course_loc]));

        }
        else {
            drawable.setColor(Color.parseColor(colors[ScheduleFragment.index]));
            ScheduleFragment.index = (ScheduleFragment.index + 1) % colors.length;
            ScheduleFragment.courses.add(course);
        }

        textView.setBackground(drawable);

        textView.setTextColor(Color.WHITE);
		textView.setPadding(5, 5, 5, 5);
		textView.setTextSize(12);
		textView.setOnClickListener((v) -> {
			ScheduleInfoFragment fragment = new ScheduleInfoFragment();
			Bundle bundle = setBundle();
			fragment.setArguments(bundle);
			activity.getFragmentManager().beginTransaction().add(fragment, "1").commit();
		});
		ScheduleFragment.textViewList.add(textView);
		return textView;
	}

	private boolean isCourseExist(String course) {
	    return ScheduleFragment.courses.contains(course);
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
	private GridLayout.LayoutParams setLayoutParams() {
		GridLayout.Spec rowSpec = GridLayout.spec(startTime, length, 2f);
		GridLayout.Spec columnSpec = GridLayout.spec(Integer.parseInt(day), 1, 1f);
		GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
		layoutParams.width = 0;
		layoutParams.height = 0;
		layoutParams.setMargins(2, 2, 2, 2);
		return layoutParams;
	}

	private int getLength() {
		return endTime - startTime + 1;
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
		private String mLastWeek;

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

		public Builder lastWeek(String value) {
			this.mLastWeek = value;
			return this;
		}

		public SetScheduleInfo build() {
			return new SetScheduleInfo(this);
		}
	}
}
