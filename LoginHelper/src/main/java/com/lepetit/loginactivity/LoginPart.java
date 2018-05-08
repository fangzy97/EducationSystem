package com.lepetit.loginactivity;

import android.util.Log;

import com.lepetit.eventmessage.GetLtEvent;
import com.lepetit.eventmessage.LoginEvent;
import com.lepetit.okhttphelper.OKHttpUnit;
import com.lepetit.stringHelper.StringCollection;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.jar.Attributes;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginPart {
    private String lt;
    private static LoginPart instance;

    public static void getLt() {
        OKHttpUnit.setInstance();
        getInstance()._getLt();
    }

    public static void postData(String userName, String password) {
        getInstance()._postData(userName, password);
    }

    private static LoginPart getInstance() {
        if (instance == null) {
            instance = new LoginPart();
        }
        return instance;
    }

    private LoginPart() {}

    private void sendLtEvent() {
        if (lt != null) {
            EventBus.getDefault().post(new GetLtEvent(true));
        }
        else {
            EventBus.getDefault().post(new GetLtEvent(false));
        }
    }

    private String getHiddenValue(String html) throws IOException {
        Document document = Jsoup.parse(html);
        Element element = document.select("input[type=hidden]").first();
        return element.attr("value");
    }

    private FormBody addFormBody(String userName, String password) {
        FormBody body = new FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("lt", lt)
                .add("execution", StringCollection.execution)
                .add("_eventId", StringCollection._eventId)
                .add("rmShown", StringCollection.rmShown)
                .build();
        return body;
    }

    private Headers addHeaders() {
        Headers headers = new Headers.Builder()
                .add("User-Agent", StringCollection.userAgent)
                .add("Referer", StringCollection.reference)
                .build();
        return headers;
    }

    private void _getLt() {
        OKHttpUnit.getAsync(StringCollection.loginUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lt = null;
                System.out.println(e.getMessage());
                sendLtEvent();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                lt = getHiddenValue(response.body().string());
                sendLtEvent();
                System.out.println("lt = " + lt);
            }
        });
    }

    private void _postData(String userName, String password) {
        OKHttpUnit.postAsync(StringCollection.loginUrl, addFormBody(
                userName, password), addHeaders(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Login Error");
                System.out.println(e.getMessage());
                EventBus.getDefault().post(new LoginEvent(-1));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendLoginEvent(isLoginSuccessful(response.body().string()));
            }
        });
    }

    private boolean isLoginSuccessful(String html) throws IOException {
        Document document = Jsoup.parse(html);
        System.out.println(document);
        Element element = document.getElementById("username");
        return element == null;
    }

    private void sendLoginEvent(Boolean isSuccessful) {
        if (isSuccessful) {
            EventBus.getDefault().post(new LoginEvent(1));
        }
        else {
            EventBus.getDefault().post(new LoginEvent(0));
        }
    }
}
