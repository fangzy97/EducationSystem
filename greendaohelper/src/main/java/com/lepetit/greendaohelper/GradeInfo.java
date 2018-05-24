package com.lepetit.greendaohelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GradeInfo {
    @Id
    private Long id;
    @Property(nameInDb = "Course")
    private String course;
    @Property(nameInDb = "Score")
    private String score;
    @Property(nameInDb = "Credit")
    private String credit;
    @Generated(hash = 1843978549)
    public GradeInfo(Long id, String course, String score, String credit) {
        this.id = id;
        this.course = course;
        this.score = score;
        this.credit = credit;
    }
    @Generated(hash = 1597538490)
    public GradeInfo() {
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
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getCredit() {
        return this.credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }
}
