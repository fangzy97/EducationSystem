package com.lepetit.eventmessage;

public class GetWeekEvent {
	private String startWeek;

	public GetWeekEvent(String startWeek) {
		this.startWeek = startWeek;
	}

	public String getStartWeek() {
		return startWeek;
	}
}
