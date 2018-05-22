package com.lepetit.schedule;

import java.util.Calendar;

public class GetTime {

    private Calendar calendar;

    GetTime() {
        calendar = Calendar.getInstance();
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }
}
