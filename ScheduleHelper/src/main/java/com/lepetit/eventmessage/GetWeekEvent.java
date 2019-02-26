package com.lepetit.eventmessage;

public class GetWeekEvent {
	private String startWeek;
	private String endWeek;

	public GetWeekEvent(String startWeek, String endWeek) {
		this.startWeek = startWeek;
		this.endWeek = endWeek;
	}

	public String getStartWeek() {
		return startWeek;
	}

    public String getEndWeek() {
        return endWeek;
    }
}
