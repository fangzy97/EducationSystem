package com.lepetit.messagehelper;

public class ConnectEvent {
    private final boolean isSuccessful;

    public ConnectEvent(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public boolean isSuccessful() {

        return isSuccessful;
    }
}
