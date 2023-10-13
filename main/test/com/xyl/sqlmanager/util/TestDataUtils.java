package com.xyl.sqlmanager.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDataUtils {
    long start;

    @Before
    public void before(){
        start = System.currentTimeMillis();
    }

    @Test
    public void test1time(){
        String data = DataUtils.timestameToString(System.currentTimeMillis());
        System.out.println(data);
    }

    @Test
    public void test100time(){
        for(int i = 0 ; i < 100 ;i++){
            String data = DataUtils.timestameToString(System.currentTimeMillis());
            System.out.println(data);
        }
    }

    @After
    public void end(){
        System.out.println("耗时:"+(System.currentTimeMillis()-start));
    }

}
