package com.lepetit.schedulehelper;

import com.lepetit.eventmessage.GetWeekEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

class AnalyzeWeekInfo {
	private static AnalyzeWeekInfo instance;

	private AnalyzeWeekInfo() {}

	private void _analyze(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByTag("td");
		List<String> allTime = elements.eachAttr("title");
		String startWeek = allTime.get(0);
		String endWeek = allTime.get(allTime.size() - 1);
		// 替换“年”“月”为“-”
		startWeek = startWeek.replaceAll("\\u5e74", "-").replaceAll("\\u6708", "-");
		endWeek = endWeek.replaceAll("\\u5e74", "-").replaceAll("\\u6708", "-");
		EventBus.getDefault().post(new GetWeekEvent(startWeek, endWeek));
	}

	private static AnalyzeWeekInfo getInstance() {
		if (instance == null) {
			instance = new AnalyzeWeekInfo();
		}
		return instance;
	}

	static void analyze(String html) {
		getInstance()._analyze(html);
	}
}
