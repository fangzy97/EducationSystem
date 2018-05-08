package com.lepetit.eventmessage;

public class LoginEvent {
    private int loginState;

    public LoginEvent(int loginState) {
        this.loginState = loginState;
    }

    public int getLoginState() {
        return loginState;
    }
}
