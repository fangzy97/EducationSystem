package com.lepetit.schedulehelper;

import com.lepetit.messagehelper.ConnectEvent;
import com.lepetit.messagehelper.FinishEvent;
import com.lepetit.okhttphelper.OKHttpUnit;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Response;

public class Schedule {
    private static Schedule instance;

    private Schedule() {}

    private static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }

    private void _getChosenSchedule(String year) {
        Headers headers = setHeaders();
        FormBody body = setFormBody(year);
        OKHttpUnit.postAsync(StringCollection.url, body, headers, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new ConnectEvent(false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventBus.getDefault().post(new ConnectEvent(true));
                DealHtml.analyze(response.body().string());
                EventBus.getDefault().post(new FinishEvent());
            }
        });
    }

    private Headers setHeaders() {
        return new Headers.Builder()
                .add("User-Agent", StringCollection.userAgent)
                .add("Referer", StringCollection.reference)
                .build();
    }

    private FormBody setFormBody(String year) {
        return new FormBody.Builder()
                .add("cj0701id", "")
                .add("zc", "")
                .add("demo", "")
                .add("xnxq01id", year)
                .add("sfFD", "1")
                .build();
    }

    public static void getChosenSchedule(String year){
        getInstance()._getChosenSchedule(year);
    }
}
