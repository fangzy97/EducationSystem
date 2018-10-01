package com.lepetit.schedulehelper;

import com.lepetit.eventmessage.ScheduleEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        Document document = Jsoup.parse(html);
        Elements trs = document.select("table").select("tr");

        for (Element tr : trs) {
        	Elements tds = tr.select("td");

        	int day = 0;
        	for (Element td : tds) {
        		day++;
        		Elements divs = td.select("div");
        		if (divs.size() == 0) continue;

        		Element div = divs.get(1);
        		Elements fonts = div.select("font");

        		if (fonts.size() == 0) continue;

        		String string = delString(div.ownText());
        		int count = 0;
        		while (!string.isEmpty()) {
        			StringBuilder course = new StringBuilder();
        			int index = string.indexOf(" ");
        			if (index == -1) {
        				course.append(string);
						string = "";
					}
					else{
        				if (isDivideSign(string.charAt(0))) {
        					string = string.substring(index + 1);
        					continue;
						}

						course.append(string, 0, index);
						string = string.substring(index + 1);
        				if (isDivideSign(string.charAt(0))) {
							int temp = string.indexOf(" ");
							string = string.substring(temp + 1);
						}
						else {
        					while (!string.isEmpty() && !isDivideSign(string.charAt(0))) {
								int temp = string.indexOf(" ");
								if (temp == -1) {
									course.append(string);
									string = "";
								}
								else {
									course.append(string, 0, temp);
									string = string.substring(temp + 1);
								}
							}
						}
					}

					String teacher = getInfo(fonts.select("font[title=\"老师\"]"), count);
					String weekAndTime = getInfo(fonts.select("font[title=\"周次(节次)\"]"), count);
					String week = getWeek(weekAndTime);
					String time = getTime(weekAndTime);
					String lastWeek = getLastWeek(week);
					String classroom = getInfo(fonts.select("font[title=\"教室\"]"), count);

					EventBus.getDefault().post(new ScheduleEvent(String.valueOf(day), course.toString(), teacher, week, time, classroom, lastWeek));
					System.out.println(day + " " + course + " "
							+ teacher + " " + week + " " + time + " " + classroom + " " + lastWeek);

					count++;
				}
			}
		}
    }

    private boolean isDivideSign(char ch) {
    	return ch == '-';
	}

    private String getInfo(Elements elements, int index) {
    	if (index < elements.size()) {
    		return elements.get(index).ownText();
		}
		return "";
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

    private String delString(String text) {
        return text.replaceAll("-{2,}", "-").replaceAll("\\s+", " ");
    }
}
