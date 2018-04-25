package com.lepetit.greendaohelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ScheduleInfo {
    @Id
    private Long id;
    @Property(nameInDb = "Day")
    private String day;
    @Property(nameInDb = "Course")
    private String course;
    @Property(nameInDb = "Teacher")
    private String teacher;
    @Property(nameInDb = "Week")
    private String week;
    @Property(nameInDb = "Time")
    private String time;
    @Property(nameInDb = "Classroom")
    private String classroom;
    @Generated(hash = 220125685)
    public ScheduleInfo(Long id, String day, String course, String teacher,
            String week, String time, String classroom) {
        this.id = id;
        this.day = day;
        this.course = course;
        this.teacher = teacher;
        this.week = week;
        this.time = time;
        this.classroom = classroom;
    }
    @Generated(hash = 57358373)
    public ScheduleInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getCourse() {
        return this.course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public String getTeacher() {
        return this.teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getWeek() {
        return this.week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getClassroom() {
        return this.classroom;
    }
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

}
