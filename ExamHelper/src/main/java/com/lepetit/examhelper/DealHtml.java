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
					.replaceAll("\\s+", " ")
					.replaceAll("~", "°™°™")
					.substring(loc);
		}
    }

    private void getInfo() {
        while (info.length() > 0) {
            sub();
            String course = divide();
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

    private boolean isDelete(String string) {
    	string = string.replaceAll("\\u7b2c[0-9]+\\u5468", "0");
    	string = string.replaceAll("\\u661f\\u671f[0-9]", "0");
    	string = string.replaceAll("\\u7b2c[0-9]+\\u8282", "0");
    	string = string.replaceAll("\\u661f\\u671f\\u65e5", "0");
    	return string.equals("0");
	}

	private boolean isTime(String string) {
    	string = string.replaceAll("[0-9]{2}:[0-9]{2}\\u2014\\u2014[0-9]{2}:[0-9]{2}", "0");
    	return string.equals("0");
	}

    private String divide() {
    	String string;
        int loc = info.indexOf(" ");
        if (loc > -1) {
        	do {
        		string = info.substring(0, loc);
        		info = info.substring(loc + 1);
        		loc = info.indexOf(" ");
			} while (isDelete(string) && loc > -1);

        	if (loc > -1) {
        		String temp = info.substring(0, loc);
				if (isTime(temp)) {
					string += "»’" + temp;
					info = info.substring(loc + 1);
				}
			}
		}
		else {
        	string = info;
		}
        return string;
    }

    private void sub() {
        for (int i = 0; i < 3; i++) {
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
