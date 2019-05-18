package com.lepetit.gradehelper;

import com.lepetit.eventmessage.GradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class DealHtml {
    private static DealHtml instance;

    private DealHtml() {}

    private static DealHtml getInstance() {
        if (instance == null) {
            instance = new DealHtml();
        }
        return instance;
    }

    // 获取成绩
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
            String credit = tds.get(6).ownText();
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

    private Map<String, String> _analyzeGrade(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table");

        // 获取分数相关的信息
        Elements tds = elements.get(1).select("td");
        String class_number = tds.get(0).text();
        String subject_number = tds.get(1).text();
        String learn_number = tds.get(2).text();
        String ave_grade = tds.get(3).text();
        String max_grade = tds.get(4).text();

        // 获取排名信息
        tds = elements.get(2).select("td");
        String class_percent = tds.get(1).text();
        String subject_percent = tds.get(2).text();
        String learn_percent = tds.get(3).text();

        Map<String, String> infos = new HashMap<>();
        infos.put("class_number", class_number);
        infos.put("subject_number", subject_number);
        infos.put("learn_number", learn_number);
        infos.put("ave_grade", ave_grade);
        infos.put("max_grade", max_grade);
        infos.put("class_percent", class_percent);
        infos.put("subject_percent", subject_percent);
        infos.put("learn_percent", learn_percent);
        return infos;
    }

    static void analyze(String html) {
        getInstance()._analyze(html);
    }

    static Map<String, String> analyzeGrade(String html) {
        return getInstance()._analyzeGrade(html);
    }

    //一下两个函数用于测试
    static String _getScore(String score) {
        return getInstance().getScore(score);
    }
}
