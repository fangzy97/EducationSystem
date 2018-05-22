package com.lepetit.eventmessage;

public class GetYearEvent {
    private String year;

    public GetYearEvent(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }
}
