package com.lepetit.eventmessage;

public class ExamEvent {
    private final String course;
    private final String time;
    private final String classroom;
    private final String seat;

    public ExamEvent(String course, String time, String classroom, String seat) {
        this.course = course;
        this.time = time;
        this.classroom = classroom;
        this.seat = seat;
    }

    public String getCourse() {
        return course;
    }

    public String getTime() {
        return time;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getSeat() {
        return seat;
    }
}
