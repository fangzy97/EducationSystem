package com.lepetit.eventmessage;

public class GradeEvent {
    private final String year;
    private final String course;
    private final String score;
    private final String credit;

    public GradeEvent(String year, String course, String score, String credit) {
        this.year = year;
        this.course = course;
        this.score = score;
        this.credit = credit;
    }

	public String getYear() {
		return year;
	}

	public String getCourse() {
        return course;
    }

    public String getScore() {
        return score;
    }

    public String getCredit() {
        return credit;
    }
}
