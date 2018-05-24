package com.lepetit.gettimehelper;

import java.util.ArrayList;
import java.util.List;

public class GetTimeInfo {
    private GetTime getTime;
    private int year;
    private int month;
    private static GetTimeInfo instance;

    private GetTimeInfo() {
        getTime = new GetTime();
        year = getTime.getYear();
        month = getTime.getMonth();
    }

    private static GetTimeInfo getInstance() {
        if (instance == null) {
            instance = new GetTimeInfo();
        }
        return instance;
    }

    private List<String> _getTimeList() {
        List<String> list = new ArrayList<>();

        for (int i = 2014; i < year; i++) {
            String string1 = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-1";
            String string2 = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-2";
            list.add(string1);
            list.add(string2);
        }

        if (month > 7) {
            String string = String.valueOf(year) + "-" + String.valueOf(year + 1) + "-1";
            list.add(string);
        }
        return list;
    }

    private String _getSimpleTime() {
        if (month > 7) {
            return String.valueOf(year) + "-" + String.valueOf(year + 1) + "-1";
        }
        else {
            return String.valueOf(year - 1) + "-" + String.valueOf(year) + "-2";
        }
    }

    public static List<String> getTimeList() {
        return getInstance()._getTimeList();
    }

    public static String getSimpleSTime() {
        return getInstance()._getSimpleTime();
    }
}
