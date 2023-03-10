package com.xyl.sqlmanager;

import com.mysql.cj.xdevapi.Result;
import com.xyl.sqlmanager.util.SqlStringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    /**
     * 显示表的所有行
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<Map<String,String>> select(Connection connection, String tableName, Map<String,String> where, List<String> columns) throws SQLException {

        List<Map<String,String>> map = new ArrayList<>();
        String sql="";
        if(columns != null)
            sql = "SELECT "+ SqlStringUtil.toColumn(columns)+" FROM "+tableName;
        else
            sql = "SELECT * FROM "+tableName;
        Statement statement = connection.createStatement();
        if(statement.execute(sql)){
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                Map<String,String> item = new TreeMap<String, String>();
                for(int i = 0 ; i < columns.size() ; i++){
                    item.put(columns.get(i),resultSet.getString(columns.get(i)));
                }
                map.add(item);
            }
        }
        return map;
    }

    /**
     * 从表中删除行数据
     * @param connection
     * @param tableName
     * @param where where条件
     * @return
     * @throws SQLException
     */
    public boolean delete(Connection connection, String tableName,Map<String,String> where,List<Boolean> andOr) throws SQLException {
        Statement statement = connection.createStatement();
        String strSql = "DELETE FROM " + tableName + " WHERE"+SqlStringUtil.toAndWhereNotNull(where,andOr );
        statement.execute(strSql);
        return true;
    }

    /**
     * 插入指令
     * @param connection
     * @param tableName
     * @param values
     * @return
     * @throws SQLException
     */
    public boolean insert(Connection connection,String tableName,List<String> values) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO " + tableName + SqlStringUtil.toInsert(values);
        statement.execute(sql);
        return true;
    }

    /**
     * 更新指令
     * @return
     */
    public boolean update(Connection connection, String tableName, Map<String,String> map, Map<String,String> where, List<Boolean> andOr) throws SQLException {
        Statement statement = connection.createStatement();
        String strSql = "UPDATE " + tableName + " SET"+ SqlStringUtil.toUpdate(map) +" WHERE";
        strSql = strSql+SqlStringUtil.toAndWhere(where,andOr);
        statement.execute(strSql);
        return true;
    }

    /**
     * 执行sql
     * @param sql
     * @return
     */
    public List<Map<String,String>> executeSql(Connection connection,String sql) throws SQLException {
        Statement statement = connection.createStatement();
        List<Map<String,String>> map = new ArrayList<>();
        if(statement.execute(sql)){
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                Map<String,String> item = new TreeMap<String, String>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                for(int i = 1 ; i <= columns ; i++){
                    item.put(resultSetMetaData.getColumnName(i),resultSet.getString(i));
                }
                map.add(item);
            }
        }
        return map;
    }
}
