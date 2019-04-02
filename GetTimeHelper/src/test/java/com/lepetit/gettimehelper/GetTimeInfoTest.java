package com.lepetit.gettimehelper;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetTimeInfoTest {

    @Test
    public void getPastWeek() {
        Assert.assertEquals(GetTimeInfo.getPastDay("2018-4-2", "2018-5-12"), 40);
    }
}