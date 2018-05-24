package com.lepetit.gradehelper;

import com.lepetit.eventmessage.GradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DealHtml {
    private static DealHtml instance;
    private String info;
    private int sign;

    private DealHtml() {
        sign = 0;
    }

    private static DealHtml getInstance() {
        if (instance == null) {
            instance = new DealHtml();
        }
        return instance;
    }

    private void _analyze(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("dataList");
        info = element.text();
        preDeal();
        getGradeInfo();
    }

    private void preDeal() {
        int loc = info.indexOf("1");
        if (loc == -1) {
            info = "";
        }
        else {
            info = info.substring(loc);
        }
    }

    private void getGradeInfo() {
        while (info.length() > 0) {
            sub(3);
            String course = divide();
            String score = divide();
            String credit = divide();
            sub(5);
            EventBus.getDefault().post(new GradeEvent(course, score, credit));

            System.out.println(course);
            System.out.println(score);
            System.out.println(credit);
        }
    }

    private boolean isI(char ch) {
        return ch == 'I';
    }

    private String divide() {
        int loc = info.indexOf(" ");
        String string = info.substring(0, loc);
        info = info.substring(loc + 1);
        if (sign == 0 && isI(info.charAt(0))) {
            loc = info.indexOf(" ");
            string += info.substring(0, loc);
            info = info.substring(loc + 1);
        }
        sign = (sign + 1) % 3;
        return string;
    }

    private void sub(int n) {
        for (int i = 0; i < n; i++) {
            int loc = info.indexOf(" ");
            if (loc == -1) {
                info = "";
            }
            else {
                info = info.substring(loc + 1);
            }
        }
    }

    public static void analyze(String html) {
        getInstance()._analyze(html);
    }
}
