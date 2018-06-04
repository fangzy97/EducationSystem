package com.lepetit.examhelper;

import com.lepetit.eventmessage.ExamEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class DealHtml {

    private String info;

    private void _analyze(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("dataList");
        info = element.text();
        preDeal();
        getInfo();
    }

    private void preDeal() {
        int loc = info.indexOf("1");
        if (loc > -1) {
			info = info.replaceAll("\\u5907\\u6CE8", "")
					.replaceAll("\\u5206\\u6563\\u8003\\u8BD5", "")
					.replaceAll("\\s+", " ")
					.substring(loc);
		}
    }

    private void getInfo() {
        while (info.length() > 0) {
            sub(2);
            String course = divide();
            sub(3);
            String time = divide();
            String classroom = divide();
            String seat = divide();
            EventBus.getDefault().post(new ExamEvent(course, time, classroom, seat));

            System.out.println(course);
            System.out.println(time);
            System.out.println(classroom);
            System.out.println(seat);
        }
    }

    private String divide() {
    	String string;
        int loc = info.indexOf(" ");
        if (loc > -1) {
			string = info.substring(0, loc);
			info = info.substring(loc + 1);
		}
		else {
        	string = info;
		}
        return string;
    }

    private void sub(int n) {
        for (int i = 0; i < n; i++) {
            int loc = info.indexOf(" ");
            if (loc > -1) {
				info = info.substring(loc + 1);
			}
        }
    }

    public void analyze(String html) {
        _analyze(html);
    }
}
