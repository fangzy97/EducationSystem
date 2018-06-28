package com.lepetit.schedulehelper;

import com.lepetit.eventmessage.ScheduleEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealHtml {
    private String info;
    private int sign;
    private static DealHtml instance;

    private DealHtml() {};

    private static DealHtml getInstance() {
        if (instance == null) {
            instance = new DealHtml();
        }
        return instance;
    }

    public static void analyze(String html) throws IOException {
        getInstance()._analyze(html);
    }

    private void _analyze(String html) throws IOException {
        sign = 0;
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input[name=jx0415zbdiv_2]");
        for (int i = 0; i < elements.size(); i++) {
            Element element = document.getElementById(elements.get(i).attr("value"));
            if (element.children().size() > 0) {
                int temp = i % 7 + 1;
                String day = String.valueOf(temp);
                info = delString(element.text());
                while (info.length() > 0) {
                    String course = divideString();
                    String teacher = divideString();
                    String weekAndTime = divideString();
                    String week = getWeek(weekAndTime);
                    String time = getTime(weekAndTime);
                    String classroom = divideString();
                    String lastWeek = getLastWeek(week);
                    EventBus.getDefault().post(new ScheduleEvent(day, course, teacher, week, time, classroom, lastWeek));
                    System.out.println(day + " " + course + " "
							+ teacher + " " + week + " " + time + " " + classroom + " " + lastWeek);
                }
            }
        }
    }

    private String getLastWeek(String week) {
    	List<String> list = new ArrayList<>();
    	week = week.substring(0, week.indexOf("("));
    	String temp = week;
		String regex = "(\\d+)";

		Pattern pattern = Pattern.compile(regex);
		while (temp.length() > 0) {
			Matcher matcher = pattern.matcher(temp);
			if (matcher.find()) {
				list.add(matcher.group(0));
				int index = matcher.end();
				if (index < temp.length()) {
					temp = temp.substring(index + 1);
				}
				else break;
			}
		}
		return addWeekToList(list, week);
	}

	private String addWeekToList(List<String> list, String week) {
    	int start = 0, end, index;
    	String result = "";
    	for (String s : list) {
    		index = week.indexOf(s);
    		if (index > 0) {
    			if (week.charAt(index - 1) == ',') {
    				start = Integer.valueOf(s);
					result = result.concat(s + ";");
				}
				else if (week.charAt(index - 1) == '-'){
    				end = Integer.valueOf(s);
    				for (int i = start + 1; i <= end; i++) {
    					result = result.concat(String.valueOf(i) + ";");
					}
				}
			}
			else {
    			start = Integer.valueOf(s);
				result = result.concat(s + ";");
			}
		}
		return result;
	}

    private String getWeek(String string) {
        return string.substring(0, string.indexOf("["));
    }

    private String getTime(String string) {
        return string.substring(string.indexOf("[") + 1, string.length() - 1);
    }

    private String divideString() {
        int index = info.indexOf(" ");

        String string;
        if (index == -1) {
            string = info;
            info = info.substring(info.length());
		}
        else {
        	if (sign == 0) {
        		string = info.substring(0, index);
        		info = info.substring(index + 1);

        		if (isI(info.charAt(0))) {
        			int temp = info.indexOf(" ");
        			string += " " + info.substring(0, temp);
        			info = info.substring(temp + 1);
				}
			}
			else if (sign == 1) {
        		if (isNumber(info.charAt(0))) {
        			string = " ";
				}
				else {
					string = info.substring(0, index);
					info = info.substring(index + 1);

					if (isLetter(info.charAt(0))) {
						int temp = info.indexOf(" ");
						string += " " + info.substring(0, temp);
						info = info.substring(temp + 1);
					}
					else if (isExtraWord(string)) {
						int temp = info.indexOf(" ");
						string = info.substring(0, temp);
						info = info.substring(temp + 1);
					}
				}
			}
			else if (sign == 3) {
        		if (isClassroom(info.substring(0, 3))) {
        			string = info.substring(0, index);
        			info = info.substring(index + 1);
				}
				else {
        			string = " ";
				}
			}
			else {
        		string = info.substring(0, index);
        		info = info.substring(index + 1);
			}
        }
        sign = (sign + 1) % 4;
        return string;
    }

    private boolean isExtraWord(String string) {
    	string = string.replaceAll("\\u8bf7\\u5c3d\\u91cf[\\u4e00-\\u9fa5]*", "0");
    	return string.equals("0");
	}

    private boolean isClassroom(String string) {
    	string = string.replaceAll("\\u7530\\u5f84\\u573a", "0");		//良乡田径场
    	string = string.replaceAll("\\u4fe1[0-9]+", "0");				//信教
		string = string.replaceAll("\\u826f\\u4e61[0-9]", "0");		//良乡
		string = string.replaceAll("\\u81f3\\u5584\\u56ed", "0");		//至善园
		string = string.replaceAll("\\u4f53\\u80b2\\u9986", "0");		//体育馆
		string = string.replaceAll("\\u897f\\u5c71\\u8bd5", "0");		//西山试验场
		string = string.replaceAll("[0-9]-[0-9]", "0");					//3,5,7教
		string = string.replaceAll("\\u73bb\\u7483\\u5de5", "0");		//玻璃工房
		string = string.replaceAll("\\u4e2d[0-9][0-9]", "0");			//中教
		string = string.replaceAll("\\u5b87\\u822a\\u5927", "0");		//宇航大楼
		string = string.replaceAll("\\u7eb3\\u7c73\\u5149", "0");		//纳米光子学材料与技术实验室
		string = string.replaceAll("\\u7f51\\u7403\\u573a", "0");		//网球场
    	return string.equals("0");
	}

    private boolean isLetter(char ch) {
    	return ch >= 'A' && ch <= 'Z';
	}

    private boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isI(char ch) {
        return ch == 'I';
    }

    private String delString(String text) {
        return text.replaceAll("-{2,}", "").replaceAll("\\s+", " ");
    }
}
