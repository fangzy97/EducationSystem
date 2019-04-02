package com.lepetit.gradehelper;

import org.junit.Assert;
import org.junit.Test;

public class DealHtmlTest {

    @Test
    public void analyze(){

    }

    @Test
    public void _getScore() {
        String score = DealHtml._getScore("91.5");
        Assert.assertEquals(score, "91.5");
    }
}