package com.xyl.sqlmanager.util;

import com.xyl.sqlmanager.entity.ConnectInfo;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSqlStringUtil {

    @Test
    public void testToWhere(){
        Map<String,String> map = new HashMap<>();
        map.put("id","234");
        map.put("name","jsdkfj");
        System.out.println(SqlStringUtil.toAndWhere(map, Arrays.asList(false,true)));
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

        System.out.println(SqlStringUtil.toColumn(Arrays.asList("file","filename","fileId")));
    }

    @Test
    public void getWhereCondition(){
        List<String> sqlList = new ArrayList<>();

        sqlList.add("SELECT * FROM users WHERE age > ? AND gender = ?;");
        sqlList.add("UPDATE products SET price = price + ? WHERE category = ?;");
        sqlList.add("INSERT INTO orders (customer_id, product_id, quantity) VALUES (?, ?, ?);");
        sqlList.add("DELETE FROM customers WHERE id = ?;");
        sqlList.add("SELECT * FROM employees WHERE salary > ? AND department = ?;");
        sqlList.add("UPDATE inventory SET quantity = quantity - ? WHERE product_id = ?;");
        sqlList.add("INSERT INTO transactions (user_id, amount, date) VALUES (?, ?, ?);");
        sqlList.add("DELETE FROM orders WHERE order_id = ?;");
        sqlList.add("SELECT * FROM products WHERE price < ? AND category = ?;");
        sqlList.add("UPDATE users SET password = ? WHERE username = ?;");

        for(String s:sqlList){
            getCOlumn(s);
        }
    }

    public void getCOlumn(String sql){
        // Define a regex pattern to match column names in the WHERE condition
        Pattern pattern = Pattern.compile("(?i)\\bWHERE\\s+(.*?)\\s*;");

        // Use a Matcher to find column names in the WHERE condition
        Matcher matcher = pattern.matcher(sql);

        // Iterate through matches and extract column names
        while (matcher.find()) {
            String whereCondition = matcher.group(1);
            String[] columnNames = whereCondition.split("\\s+(AND|OR|;|,)\\s+");
            for (String columnName : columnNames) {
                System.out.print("字段名称:" + columnName);
            }
        }
        System.out.println();
    }
    public void getColumn(String sql){

        // Define a regex pattern to match column names in the WHERE condition
        Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*\\?");

        // Use a Matcher to find column names in the WHERE condition
        Matcher matcher = pattern.matcher(sql);

        // Iterate through matches and extract column names
        while (matcher.find()) {
            String columnName = matcher.group(1);
            System.out.print("找出的字段名称: " + columnName);
        }
        System.out.println();
    }

    @Test
    public void tobytes() throws UnsupportedEncodingException {

        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setHost("localhost");
        connectInfo.setUser("root");
        connectInfo.setPass("123456");
        connectInfo.setPort(3306);
        connectInfo.setName("test连接");
        connectInfo.setVersion("5.x");
        FileUtil fileUtil = new FileUtil();
        byte[] bytes = new byte[192];
        fileUtil.stringToBytes32(connectInfo.getName(),bytes,0);
        fileUtil.stringToBytes32(connectInfo.getHost(),bytes,32);
        fileUtil.intToBytes32(connectInfo.getPort(),bytes,64);
        fileUtil.stringToBytes32(connectInfo.getUser(),bytes,96);
        fileUtil.stringToBytes32(connectInfo.getPass(),bytes,128);
        fileUtil.stringToBytes32(connectInfo.getVersion(),bytes,160);
        File file = new File("aaa.jj");
        fileUtil.writeByte(bytes,file);
    }

}
