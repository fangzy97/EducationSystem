package com.lepetit.leapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MyPageAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	private String title[] = {"首页", "课程表", "考试", "成绩"};

	public MyPageAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}
}
