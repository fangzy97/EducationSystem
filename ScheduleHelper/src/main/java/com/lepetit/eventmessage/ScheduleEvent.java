package com.lepetit.eventmessage;

public class ScheduleEvent {
    private String day;
    private String course;
    private String teacher;
    private String week;
    private String time;
    private String classroom;

    public ScheduleEvent(String day, String course, String teacher, String week, String time, String classroom) {
        this.day = day;
        this.course = course;
        this.teacher = teacher;
        this.week = week;
        this.time = time;
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public String getCourse() {
        return course;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getWeek() {
        return week;
    }

    public String getTime() {
        return time;
    }

    public String getClassroom() {
        return classroom;
    }
}
