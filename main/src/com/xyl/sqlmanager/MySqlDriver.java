package com.xyl.sqlmanager;

import com.xyl.sqlmanager.db.SelectForest;
import com.xyl.sqlmanager.db.SqlGenerator;
import com.xyl.sqlmanager.exception.CustomException;
import com.xyl.sqlmanager.exception.ResponseEnum;
import com.xyl.sqlmanager.util.SqlStringUtil;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MySqlDriver extends BaseSqlDriver {

    private int version;

    public MySqlDriver(String user,String pass,String ip,String port,int version) throws ClassNotFoundException {
        //版本兼容
        if(version<=5) {
            setDriverClass("com.mysql.jdbc.Driver");
        }
        else if(version>=8){
            setDriverClass("com.mysql.cj.jdbc.Driver");
        }

        //初始化connect信息
        setUser(user);
        setPass(pass);
        setVersion(version);
            setUrl("jdbc:mysql://" + ip + ":"+port+"/");

        Class.forName(driverClass);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * 显示表的所有行
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<Map<String,String>> select(Connection connection, String tableName,Map<String,String> where,List<String> columns) {

        List<Map<String,String>> map = new ArrayList<>();
        SelectForest forest = new SelectForest();
        if(columns != null) {
            forest.getRoot().insertLeft(SqlStringUtil.toColumn(columns));
            forest.getRoot().insertRight("from").insertLeft(tableName);
        }
        else {
            forest.insertLeaf("select","*");
            forest.insertNode("from").insertLeft(tableName);
        }
        //多页查询
        forest.insertNode("LIMIT").insertLeft("0,").insertLeft("100");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            if(statement.execute(forest.generate())){
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    Map<String,String> item = new TreeMap<String, String>();
                    for(int i = 0 ; i < columns.size() ; i++){
                        item.put(columns.get(i),resultSet.getString(columns.get(i)));
                    }
                    map.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public boolean update(Connection connection,String tableName,Map<String,String> map,Map<String,String> where,List<Boolean> andOr) throws SQLException {
        Statement statement = connection.createStatement();
        String strSql = "UPDATE " + tableName + " SET"+SqlStringUtil.toUpdate(map) +" WHERE";
        strSql = strSql+SqlStringUtil.toAndWhere(where,andOr);
        statement.execute(strSql);
        return true;
    }

    /**
     * 执行sql
     * @param sql
     * @return
     */
    public List<Map<String,String>> executeSql(Connection connection,String sql)  {
        try {
            Statement statement = connection.createStatement();
            List<Map<String, String>> map = new ArrayList<>();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    Map<String, String> item = new TreeMap<String, String>();
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columns = resultSetMetaData.getColumnCount();
                    for (int i = 1; i <= columns; i++) {
                        item.put(resultSetMetaData.getColumnName(i), resultSet.getString(i));
                    }
                    map.add(item);
                }
            }
            return map;
        }
        catch (SQLException e){
            throw new CustomException(ResponseEnum.CUS_SQL_EXCEPTION.getCode(),ResponseEnum.CUS_SQL_EXCEPTION.getMes()+e.getMessage());
        }
    }


}
