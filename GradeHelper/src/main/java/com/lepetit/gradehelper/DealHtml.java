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
			String analyze = tds.get(15).select("a").attr("onclick");
			analyze = getGradeAnalyzeAddress(analyze);
			EventBus.getDefault().post(new GradeEvent(year, course, score, credit, analyze));

			System.out.println(year);
			System.out.println(course);
			System.out.println(score);
			System.out.println(credit);
		}
    }

    // 从网页中获取成绩
    private String getScore(String score) {
        // 匹配数字的正则表达式
        String pattern = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
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

    // 成绩分析功能原本是一个js的函数调用，其地址包含在函数参数中
    // 该函数将地址从参数中取出
    private String getGradeAnalyzeAddress(String analyzeAddress) {
        int start = analyzeAddress.indexOf("'");
        int end = analyzeAddress.lastIndexOf("'");
        return StringCollection.baseUrl + analyzeAddress.substring(start + 1, end);
    }

    static void analyze(String html) {
        getInstance()._analyze(html);
    }

    static String _getScore(String score) {
        return getInstance().getScore(score);
    }
}
