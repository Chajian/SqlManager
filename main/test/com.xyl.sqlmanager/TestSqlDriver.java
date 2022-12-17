package com.xyl.sqlmanager;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestSqlDriver {

    MySqlDriver mySqlDriver;

    @Before
    public void init() throws ClassNotFoundException {
        mySqlDriver = new MySqlDriver("root","123456789","","localhost",8);
    }


    @Test
    public void testShowTables() throws ClassNotFoundException, SQLException {

        Connection connection = mySqlDriver.getConnection();

        mySqlDriver.useDataBase(connection,"Chat");
        List<String> list = mySqlDriver.showTables(connection);
        assert list!=null: "查询失败";

        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    public void testShowDatabasesName() throws ClassNotFoundException, SQLException {
        Connection connection = mySqlDriver.getConnection();
        List<String> list = mySqlDriver.showDatabases(connection);
        assert list!=null: "查询失败";

        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    public void testShowFieldsFromTable() throws SQLException {
        Connection connection = mySqlDriver.getConnection();
        mySqlDriver.useDataBase(connection,"Chat");
        List list = mySqlDriver.showTableColumnNames(connection,"file");
        assert list!=null: "查询失败";
        System.out.println(Arrays.toString(list.toArray()));
    }






}
