package com.lepetit.examhelper;

import com.lepetit.gettimehelper.GetTimeInfo;
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

public class GetExamInfo {
    private static GetExamInfo instance;

    private GetExamInfo() {}

    private static GetExamInfo getInstance() {
        if (instance == null) {
            instance = new GetExamInfo();
        }
        return instance;
    }

    private void _get() {
        String time = GetTimeInfo.getSimpleSTime();
        Headers headers = setHeaders();
        FormBody body = setFormBody(time);
        OKHttpUnit.postAsync(StringCollection.url, body, headers, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
                EventBus.getDefault().post(new ConnectEvent(false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventBus.getDefault().post(new ConnectEvent(true));
                DealHtml dealHtml = new DealHtml();
                dealHtml.analyze(response.body().string());
                EventBus.getDefault().post(new FinishEvent());
            }
        });
    }

    private FormBody setFormBody(String time) {
        return new FormBody.Builder()
                .add("xqlbmc", "")
                .add("xnxqid", time)
                .add("xqlb", "")
                .build();
    }

    private Headers setHeaders() {
        return new Headers.Builder()
                .add("Referer", StringCollection.reference)
                .add("User-Agent", StringCollection.userAgent)
                .build();
    }

    public static void get() {
        getInstance()._get();
    }
}
