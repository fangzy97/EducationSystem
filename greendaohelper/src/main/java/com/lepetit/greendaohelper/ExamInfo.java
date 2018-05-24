package com.lepetit.greendaohelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExamInfo {
    @Id
    private Long id;
    @Property(nameInDb = "Course")
    private String course;
    @Property(nameInDb = "Time")
    private String time;
    @Property(nameInDb = "Classroom")
    private String classroom;
    @Property(nameInDb = "Seat")
    private String seat;
    @Generated(hash = 1942689162)
    public ExamInfo(Long id, String course, String time, String classroom,
            String seat) {
        this.id = id;
        this.course = course;
        this.time = time;
        this.classroom = classroom;
        this.seat = seat;
    }
    @Generated(hash = 467552702)
    public ExamInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCourse() {
        return this.course;
    }
    public void setCourse(String course) {
        this.course = course;
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
    public String getSeat() {
        return this.seat;
    }
    public void setSeat(String seat) {
        this.seat = seat;
    }
}
