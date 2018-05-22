package com.lepetit.schedulehelper;

import com.lepetit.eventmessage.GetYearEvent;
import com.lepetit.eventmessage.ScheduleEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DealHtml {
    private String info;
    private int sign;
    private static DealHtml instance;

    private DealHtml() {};

    private static DealHtml getInstance() {
        if (instance == null) {
            instance = new DealHtml();
        }
        return instance;
    }

    public static void analyze(String html) throws IOException {
        getInstance()._analyze(html);
    }

    private void _analyze(String html) throws IOException {
        sign = 0;
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input[name=jx0415zbdiv_2]");
        for (int i = 0; i < elements.size(); i++) {
            Element element = document.getElementById(elements.get(i).attr("value"));
            if (element.children().size() > 0) {
                int temp = i % 7 + 1;
                String tesxt = element.text();
                String day = String.valueOf(temp);
                info = delString(element.text());
                while (info.length() > 0) {
                    String course = divideString();
                    String teacher = divideString();
                    String weekAndTime = divideString();
                    String week = getWeek(weekAndTime);
                    String time = getTime(weekAndTime);
                    String classroom = divideString();
                    EventBus.getDefault().post(new ScheduleEvent(day, course, teacher, week, time, classroom));
                    System.out.println(day + " " + course + " " + teacher + " " + week + " " + time + " " + classroom);
                }
            }
        }
    }

    private String getWeek(String string) {
        return string.substring(0, string.indexOf("["));
    }

    private String getTime(String string) {
        return string.substring(string.indexOf("[") + 1, string.length() - 1);
    }

    private String divideString() {
        int index = info.indexOf(" ");

        String string;
        if (index == -1) {
            string = info;
            info = info.substring(info.length());
        }
        else {
            if (sign != 1) {
                string = info.substring(0, index);
                info = info.substring(index + 1);
                if (sign == 0) {
                    if (isLetter(info.charAt(0))) {
                        int temp = info.indexOf(" ");
                        string += " " + info.substring(0, temp);
                        info = info.substring(temp + 1);
                    }
                }
            }
            else {
                if (isNumber(info.charAt(0))) {
                    string = " ";
                }
                else {
                    string = info.substring(0, index);
                    info = info.substring(index + 1);
                }
            }
        }
        sign = (sign + 1) % 4;
        return string;
    }

    private boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isLetter(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    private String delString(String text) {
        return text.replaceAll("-{2,}", "").replaceAll("\\s+", " ");
    }
}
