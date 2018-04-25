package com.lepetit.schedulehelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DealHtml {
    private String info;
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
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input[name=jx0415zbdiv_2]");
        for (int i = 0; i < elements.size(); i++) {
            Element element = document.getElementById(elements.get(i).attr("value"));
            if (element.children().size() > 0) {
                int temp = i % 7 + 1;
                String day = String.valueOf(temp);
                info = delString(element.text());
                while (info.length() > 0) {
                    String course = divideString();
                    String teacher = divideString();
                    String weekAndTime = divideString();
                    String week = getWeek(weekAndTime);
                    String time = getTime(weekAndTime);
                    String classroom = divideString();
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
            string = info.substring(0, index);
            info = info.substring(index + 1);
        }
        return string;
    }

    private String delString(String text) {
        return text.replaceAll("-{2,}", "").replaceAll("\\s+", " ");
    }
}
