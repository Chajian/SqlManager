package com.xyl.sqlmanager;

import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础的数据库驱动
 */
public class BaseSqlDriver implements SqlDriver {

    public static String user,pass,url;
    public static String driverClass;


    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        BaseSqlDriver.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        BaseSqlDriver.pass = pass;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        BaseSqlDriver.url = url;
    }

    public static String getDriverClass() {
        return driverClass;
    }

    public static void setDriverClass(String driverClass) {
        BaseSqlDriver.driverClass = driverClass;
    }

    /**
     * 获取Connection
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,pass);
        return connection;
    }


    /**
     * 显示表名
     * @param connection
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> showTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "show tables";
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
    public boolean useDataBase(Connection connection,String db) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "use "+db;
        if(statement.execute(sql)){
            return true;
        }
        return false;
    }

    /**
     * 显示数据库
     * @param connection
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> showDatabases(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "show databases";
        if(statement.execute(sql)){
            ResultSet resultSet = statement.getResultSet();
            List<String> databasesNames = new ArrayList<>();
            while(resultSet.next()){
                //获取table名
                String databaseName = resultSet.getString(1);
                databasesNames.add(databaseName);
            }
            return databasesNames;
        }
        return null;
    }

    /**
     * 显示列名
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> showTableColumnNames(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "describe "+tableName;
        if (statement.execute(sql)) {
            ResultSet resultSet = statement.getResultSet();
            List<String> fields = new ArrayList<>();
            while(resultSet.next()){
                //获取table名
                String feild = resultSet.getString(1);
                fields.add(feild);
            }
            return fields;
        }
        return null;
    }
}
