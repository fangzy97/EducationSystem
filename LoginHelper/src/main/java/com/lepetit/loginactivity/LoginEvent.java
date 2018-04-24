package com.lepetit.loginactivity;

public class LoginEvent {
    private boolean isLoginSuccessful;

    public LoginEvent(boolean isLoginSuccessful) {
        this.isLoginSuccessful = isLoginSuccessful;
    }

    public boolean isLoginSuccessful() {
        return isLoginSuccessful;
    }
}
