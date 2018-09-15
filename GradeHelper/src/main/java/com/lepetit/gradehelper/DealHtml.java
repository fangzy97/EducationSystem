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
            info = info.substring(loc)
                    .replaceAll("\\u662f", "")
					.replaceAll("\\u5426", "");
        }
    }

    private void getGradeInfo() {
        while (info.length() > 0) {
            sub(1);
            String year = divide();
            sub(1);
            String course = divide();
            String score = divide();
            String credit = divide();
            sub(6);
            sub();

            EventBus.getDefault().post(new GradeEvent(year, course, score, credit));

			System.out.println(year);
            System.out.println(course);
            System.out.println(score);
            System.out.println(credit);
        }
    }

    private boolean isWord(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private String divide() {
        int loc = info.indexOf(" ");
        String string = info.substring(0, loc);
        info = info.substring(loc + 1);
        if (sign == 1 && isWord(info.charAt(0))) {
            loc = info.indexOf(" ");
            string += info.substring(0, loc);
            info = info.substring(loc + 1);
        }
        sign = (sign + 1) % 4;
        return string;
    }

    private void sub() {
    	boolean flag = false;
    	for (int i = 0; i < info.length(); i++) {
    		if (info.charAt(i) >= '0' && info.charAt(i) <= '9') {
    			info = info.substring(i);
    			flag = true;
    			break;
			}
		}
		if (!flag) {
    		info = "";
		}
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
