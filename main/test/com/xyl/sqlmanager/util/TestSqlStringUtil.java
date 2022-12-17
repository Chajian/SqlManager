package com.xyl.sqlmanager.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSqlStringUtil {

    @Test
    public void testToWhere(){
        Map<String,String> map = new HashMap<>();
        map.put("id","234");
        map.put("name","jsdkfj");
        System.out.println(SqlStringUtil.toAndWhere(map,List.of(false,true)));
    }

    @Test
    public void testToUpdate(){
        Map<String,String> map = new HashMap<>();
        map.put("id","234");
        map.put("name","jsdkfj");
        System.out.println(SqlStringUtil.toUpdate(map));
    }

    @Test
    public void testToColumn(){

        System.out.println(SqlStringUtil.toColumn(List.of("file","filename","fileId")));
    }

}
