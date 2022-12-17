package com.xyl.sqlmanager;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TestMysqlDriver {

    MySqlDriver mySqlDriver;

    @Before
    public void init() throws ClassNotFoundException {
        mySqlDriver = new MySqlDriver("root","123456789","","localhost",8);
    }



    @Test
    public void testShowRowsFromTable() throws SQLException {
        Connection connection = mySqlDriver.getConnection();
        mySqlDriver.useDataBase(connection,"Chat");
        List<String> columns = mySqlDriver.showTableColumnNames(connection,"file");
        List<Map<String,String>> list = mySqlDriver.select(connection,"file",null,columns);
        //利用两次steam流遍历Map表
        list.stream()
                .map(e->{
                    return Arrays.toString(e.keySet().stream()
                            .map(e1->{
                                return e1.toString()+":"+e.get(e1.toString());
                            })
                            .collect(Collectors.toList()).toArray());
                })
                .forEach(System.out::println);
    }


    @Test
    public void testDeleteRowsFromTable() throws SQLException {
        Connection connection = mySqlDriver.getConnection();
        mySqlDriver.useDataBase(connection,"Chat");
        Map<String,String> params = new HashMap<>();
        params.put("id","1");
        List<Boolean> ands = new ArrayList<>();
        params.keySet().stream()
                .forEach(
                        e->{ands.add(true);}
                );

        assert mySqlDriver.delete(connection,"message",params,ands): "删除失败！";

        System.out.println("删除成功!");
    }

    @Test
    public void testInsert() throws SQLException {
        List<String> values = new ArrayList<>();
        values.add(null);
        values.add("4");
        values.add("1");
        values.add("消息");
        values.add(new Date(Calendar.getInstance().getTimeInMillis()).toString());
        Connection connection = mySqlDriver.getConnection();
        mySqlDriver.useDataBase(connection,"Chat");
        mySqlDriver.insert(connection,"message",values);

    }

    @Test
    public void testUpdate() throws SQLException {
        Map<String,String> values = new HashMap<>();
        values.put("msg","消息咯123123123");
        Map<String,String> where = new HashMap<>();
        where.put("id","79");
        Connection connection = mySqlDriver.getConnection();
        mySqlDriver.useDataBase(connection,"Chat");
        mySqlDriver.update(connection,"message",values,where,List.of(true));
    }


}
