package com.xyl.sqlmanager;

import com.xyl.sqlmanager.util.SqlStringUtil;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * sqlite驱动
 */
public class SqliteDriver extends BaseSqlDriver{
    String name;

    public SqliteDriver(File sqliteFile) throws ClassNotFoundException {
        setDriverClass("org.sqlite.JDBC");
        setUrl("jdbc:sqlite:"+sqliteFile.getAbsolutePath());
        name = sqliteFile.getName();
        Class.forName(driverClass);
    }

    @Override
    public List<String> showDatabases(Connection connection) throws SQLException {
        return Arrays.asList(name);
    }

    @Override
    public List<String> showTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        if(statement.execute(sql)){
            ResultSet resultSet = statement.getResultSet();
            List<String> tableNames = new ArrayList<>();
            while(resultSet.next()){
                //获取table名
                String tableName = resultSet.getString(1);
                tableNames.add(tableName);
            }
            return tableNames;
        }
        return null;
    }

    @Override
    public boolean useDataBase(Connection connection, String db) throws SQLException {
        return true;
    }

    @Override
    public List<String> showTableColumnNames(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "PRAGMA table_info("+tableName+")";
        if (statement.execute(sql)) {
            ResultSet resultSet = statement.getResultSet();
            List<String> fields = new ArrayList<>();
            while(resultSet.next()){
                //获取table名
                String feild = resultSet.getString(2);
                fields.add(feild);
            }
            return fields;
        }
        return null;
    }
}
