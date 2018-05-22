package com.lepetit.schedule;

public class GetScheduleEvent {
    private final boolean isStart;

    GetScheduleEvent(boolean isFinish) {
        this.isStart = isFinish;
    }

    public boolean isFinish() {
        return isStart;
    }
}
