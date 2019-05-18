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

    // ��ȡ�ɼ�
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

    // ����ҳ�л�ȡ�ɼ�
    private String getScore(String score) {
        // ƥ�����ֵ�������ʽ
        String pattern = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
        // ��ѯ�������Ƿ�������
        boolean isHaveNumber = Pattern.matches(pattern, score);

        if (isHaveNumber) {
            return score;
        }
        else {
            // �����ĵĳɼ�ת��Ϊ����
            switch (score) {
                case "����": return "95";
                case "����": return "85";
                case "�е�": return "75";
                case "����":
                case "�ϸ�": return "65";
                case "������":
                case "���ϸ�": return "0";
            }
        }
        return "0";
    }

    // �ɼ���������ԭ����һ��js�ĺ������ã����ַ�����ں���������
    // �ú�������ַ�Ӳ�����ȡ��
    private String getGradeAnalyzeAddress(String analyzeAddress) {
        int start = analyzeAddress.indexOf("'");
        int end = analyzeAddress.lastIndexOf("'");
        return StringCollection.baseUrl + analyzeAddress.substring(start + 1, end);
    }

    private Map<String, String> _analyzeGrade(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table");

        // ��ȡ������ص���Ϣ
        Elements tds = elements.get(1).select("td");
        String class_number = tds.get(0).text();
        String subject_number = tds.get(1).text();
        String learn_number = tds.get(2).text();
        String ave_grade = tds.get(3).text();
        String max_grade = tds.get(4).text();

        // ��ȡ������Ϣ
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

    //һ�������������ڲ���
    static String _getScore(String score) {
        return getInstance().getScore(score);
    }
}
