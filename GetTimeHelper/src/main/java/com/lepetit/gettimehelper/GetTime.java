package com.lepetit.gettimehelper;

import java.util.Calendar;

class GetTime {
    private Calendar calendar;

    GetTime() {
        calendar = Calendar.getInstance();
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
