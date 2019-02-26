package com.lepetit.loginactivity;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreInfo {
    private SharedPreferences preferences;

    private static StoreInfo instance;

    private static StoreInfo getInstance() {
        if (instance == null) {
            instance = new StoreInfo();
        }
        return instance;
    }

    //初始化SharedPreferences
    private void _setPreferences(Context context) {
        preferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    //储存用户名和密码
    private void _storeInfo(String userName, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", userName);
        editor.putString("Password", password);
        editor.apply();
    }

    private void _storeStartAndEndTime(String startWeek, String endWeek) {
    	SharedPreferences.Editor editor = preferences.edit();
    	editor.putString("StartWeek", startWeek);
    	editor.putString("EndWeek", endWeek);
    	editor.apply();
	}

    //获取对应的信息
    private String _getInfo(String key) {
        return preferences.getString(key, "");
    }

    private void _clearInfo() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    //------------------------------------------------------------------------------
    //可调用的外部方法
    public static void setPreferences(Context context) {
        getInstance()._setPreferences(context);
    }

    public static void storeInfo(String userName, String password) {
        getInstance()._storeInfo(userName, password);
    }

    public static String getInfo(String key) {
        return getInstance()._getInfo(key);
    }

    public static void clearInfo() {
        getInstance()._clearInfo();
    }

    public static void storeStartAndEndTime(String startWeek, String endWeek) {
    	getInstance()._storeStartAndEndTime(startWeek, endWeek);
	}
}
