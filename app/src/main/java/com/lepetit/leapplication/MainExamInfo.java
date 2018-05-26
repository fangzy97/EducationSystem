package com.lepetit.leapplication;

public class MainExamInfo {
    private final String examTime;
    private final String course;
    private final String examLastTime;

    MainExamInfo(String examTime, String course, String examLastTime) {
        this.examTime = examTime;
        this.course = course;
        this.examLastTime = examLastTime;
    }

    public String getExamTime() {
        return examTime;
    }

    public String getCourse() {
        return course;
    }

    public String getExamLastTime() {
        return examLastTime;
    }
}
