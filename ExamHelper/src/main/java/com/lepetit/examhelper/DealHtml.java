package com.lepetit.examhelper;

import com.lepetit.eventmessage.ExamEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class DealHtml {

    private void _analyze(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("dataList");
        Elements trs = element.select("tr");
        for (Element tr : trs) {
        	Elements tds = tr.select("td");
        	if (tds.size() == 0) continue;

        	String course = tds.get(3).ownText();
			String time = tds.get(5).ownText();
			String classroom = tds.get(6).ownText();
			String seat = tds.get(7).ownText();
			EventBus.getDefault().post(new ExamEvent(course, time, classroom, seat));

			System.out.println(course);
			System.out.println(time);
			System.out.println(classroom);
			System.out.println(seat);
		}

    }

    void analyze(String html) {
        _analyze(html);
    }
}
