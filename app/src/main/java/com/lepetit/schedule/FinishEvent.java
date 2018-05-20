package com.lepetit.schedule;

public class FinishEvent {
    private final boolean isFinish;

    FinishEvent(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isFinish() {
        return isFinish;
    }
}
