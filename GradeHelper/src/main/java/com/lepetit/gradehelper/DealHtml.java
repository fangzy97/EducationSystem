package com.lepetit.gradehelper;

import com.lepetit.eventmessage.GradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DealHtml {
    private static DealHtml instance;

    private DealHtml() {}

    private static DealHtml getInstance() {
        if (instance == null) {
            instance = new DealHtml();
        }
        return instance;
    }

    private void _analyze(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("dataList");
        Elements trs = element.select("tr");
        for (Element tr : trs) {
        	Elements tds = tr.select("td");
        	if (tds.size() == 0) continue;

			String year = tds.get(1).ownText();
			String course = tds.get(3).ownText();
			String score = tds.get(4).select("a").text();
			String credit = tds.get(5).ownText();
			EventBus.getDefault().post(new GradeEvent(year, course, score, credit));

			System.out.println(year);
			System.out.println(course);
			System.out.println(score);
			System.out.println(credit);
		}
    }

    public static void analyze(String html) {
        getInstance()._analyze(html);
    }
}
