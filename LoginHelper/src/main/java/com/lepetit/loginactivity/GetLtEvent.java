package com.lepetit.loginactivity;

public class GetLtEvent {
    private boolean isSuccessful;

    public GetLtEvent(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
