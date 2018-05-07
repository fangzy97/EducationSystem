package com.lepetit.schedulehelper;

import com.lepetit.eventmessage.FinishEvent;
import com.lepetit.okhttphelper.OKHttpUnit;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Schedule {
    private final static String url = "http://jwms.bit.edu.cn/jsxsd/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_PYGL";
    private static Schedule instance;

    private Schedule() {}

    private static Schedule getInstance() {
        if (instance == null) {
            instance = new Schedule();
        }
        return instance;
    }

    private void _getSchedule() {
        OKHttpUnit.getAsync(url, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DealHtml.analyze(response.body().string());
                EventBus.getDefault().post(new FinishEvent());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public static void getSchedule() {
        getInstance()._getSchedule();
    }
}
