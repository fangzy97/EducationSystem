package com.lepetit.schedulehelper;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DealHtmlTest {

    private String html = "<table id=\"kbtable\" border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" class=\"Nsb_r_list Nsb_table\">\n" +
            "\t\t\t\t<tbody>\t\t\t\t\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t<td width=\"123\" height=\"28\" align=\"center\" valign=\"top\">\n" +
            "\t\t\t\t\t\t\t\t<input type=\"hidden\" name=\"jx0415zbdiv_1\" value=\"48EFEB7566D5432596E0E71FD6020621-1-1\">\n" +
            "\t\t\t\t\t\t\t\t<input type=\"hidden\" name=\"jx0415zbdiv_2\" value=\"48EFEB7566D5432596E0E71FD6020621-1-2\">\n" +
            "\t\t\t\t\t\t\t\t<div id=\"48EFEB7566D5432596E0E71FD6020621-1-1\" class=\"kbcontent1\" style=\"display: none;\">�����Ӳ��ϵͳ���<br><font title=\"�ܴ�(�ڴ�)\">01-2(��)</font><br><font title=\"����\">��2006</font><br></div>\n" +
            "\t\t\t\t\t\t\t\t<div id=\"48EFEB7566D5432596E0E71FD6020621-1-2\" style=\"\" class=\"kbcontent\">�����Ӳ��ϵͳ���<br><font title=\"��ʦ\">���꣨�����о�Ժ��</font><br><font title=\"�ܴ�(�ڴ�)\">1-2(��)[01-02��]</font><br><font title=\"����\">��2006</font><br></div>\n" +
            "\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t</tr>\n" +
            "</tbody></table>";

    @org.junit.Test
    public void analyze() {
        try {
            DealHtml.analyze(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getLastWeek() {
        String week =  DealHtml._getLastWeek("01-18(��)");
        System.out.println(week);
    }
}