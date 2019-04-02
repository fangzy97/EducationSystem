package com.lepetit.eventmessage;

public class GradeAnalyzeEvent {
    private final String class_number;
    private final String subject_number;
    private final String learn_number;
    private final String ave_grade;
    private final String max_grade;
    private final String class_percent;
    private final String subject_percent;
    private final String learn_percent;

    public GradeAnalyzeEvent(String class_number, String subject_number, String learn_number, String ave_grade, String max_grade, String class_percent, String subject_percent, String learn_percent) {
        this.class_number = class_number;
        this.subject_number = subject_number;
        this.learn_number = learn_number;
        this.ave_grade = ave_grade;
        this.max_grade = max_grade;
        this.class_percent = class_percent;
        this.subject_percent = subject_percent;
        this.learn_percent = learn_percent;
    }

    public String getClass_number() {
        return class_number;
    }

    public String getSubject_number() {
        return subject_number;
    }

    public String getLearn_number() {
        return learn_number;
    }

    public String getAve_grade() {
        return ave_grade;
    }

    public String getMax_grade() {
        return max_grade;
    }

    public String getClass_percent() {
        return class_percent;
    }

    public String getSubject_percent() {
        return subject_percent;
    }

    public String getLearn_percent() {
        return learn_percent;
    }
}
