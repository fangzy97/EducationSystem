package com.lepetit.gradehelper;

import com.lepetit.eventmessage.GradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

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
			String score = getScore(tds.get(4).select("a").text());
			String credit = tds.get(5).ownText();
			EventBus.getDefault().post(new GradeEvent(year, course, score, credit));

			System.out.println(year);
			System.out.println(course);
			System.out.println(score);
			System.out.println(credit);
		}
    }

    // 从网页中获取成绩
    private String getScore(String score) {
        // 匹配数字的正则表达式
        String pattern = "[0-9]+";
        // 查询数字中是否有数字
        boolean isHaveNumber = Pattern.matches(pattern, score);

        if (isHaveNumber) {
            return score;
        }
        else {
            // 将中文的成绩转换为数字
            switch (score) {
                case "优秀": return "95";
                case "良好": return "85";
                case "中等": return "75";
                case "及格":
                case "合格": return "65";
                case "不及格":
                case "不合格": return "0";
            }
        }
        return "0";
    }

    public static void analyze(String html) {
        getInstance()._analyze(html);
    }
}
