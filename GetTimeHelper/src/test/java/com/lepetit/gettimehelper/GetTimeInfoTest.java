package com.lepetit.gettimehelper;

import org.junit.Assert;
import org.junit.Test;

public class GetTimeInfoTest {

    @Test
    public void getPastWeek() {
        Assert.assertEquals(GetTimeInfo.getPastDay("2019-4-4", "2019-6-16"), 73);
    }
}