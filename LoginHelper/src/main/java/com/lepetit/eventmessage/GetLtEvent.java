package com.lepetit.eventmessage;

public class GetLtEvent {
    private boolean isSuccessful;

    public GetLtEvent(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
