package com.lepetit.loginactivity;

import android.util.Log;

import com.lepetit.okhttphelper.OKHttpUnit;
import com.lepetit.stringHelper.StringCollection;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginPart {
    private String lt;

    public void getLt() {
        OKHttpUnit.setInstance();
        _getLt();
    }

    public void postData(String userName, String password) {
        _postData(userName, password);
    }

    private void sendLtEvent() {
        if (lt != null) {
            EventBus.getDefault().post(new GetLtEvent(true));
        }
        else {
            EventBus.getDefault().post(new GetLtEvent(false));
        }
    }

    private String getHiddenValue(Response response) throws IOException {
        Document document = Jsoup.parse(response.body().string());
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
                Log.d("LoginActivity", e.getMessage());
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                lt = getHiddenValue(response);
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendLoginEvent(isLoginSuccessful(response));
            }
        });
    }

    private boolean isLoginSuccessful(Response response) throws IOException {
        Document document = Jsoup.parse(response.body().string());
        System.out.println(document);
        Element element = document.getElementById("username");
        if (element == null) {
            return true;
        }
        return false;
    }

    private void sendLoginEvent(Boolean isSuccessful) {
        if (isSuccessful) {
            EventBus.getDefault().post(new LoginEvent(true));
        }
        else {
            EventBus.getDefault().post(new LoginEvent(false));
        }
    }
}
