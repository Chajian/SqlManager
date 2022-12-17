package com.xyl.sqlmanager;

import com.xyl.sqlmanager.util.SqlStringUtil;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MySqlDriver extends BaseSqlDriver {

    private int version;

    public MySqlDriver(String user,String pass,String db,String ip,int version) throws ClassNotFoundException {
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
            setUrl("jdbc:mysql://" + ip + ":3306/" + db);

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
    public List<Map<String,String>> select(Connection connection, String tableName,Map<String,String> where,List<String> columns) throws SQLException {

        List<Map<String,String>> map = new ArrayList<>();
        String sql="";
        if(columns != null)
            sql = "SELECT "+SqlStringUtil.toColumn(columns)+" FROM "+tableName;
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
//                Map<String,String> item = new TreeMap<String, String>();
//                columns.stream()
//                        .forEach(e->{
//                            try {
//                                item.put(e.toString(),resultSet.getString(e.toString()));
//                            } catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        });
//                map.add(item);
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
//        AtomicReference<String> sql = new AtomicReference<>("DELETE FROM " + tableName + " WHERE");
        String strSql = "DELETE FROM " + tableName + " WHERE"+SqlStringUtil.toAndWhereNotNull(where,andOr );
        //提取Sql指令
//        where.keySet().stream()
//                .map(e->{
//                    return " "+e.toString()+" = "+where.get(e.toString());
//                })
//                .forEach(e->{
//                    String temp = sql.get();
//                    temp +=e.toString();
//                    sql.set(temp);
//                });
//        String strSql = sql.get();
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
        //提取Sql指令
//        for(int i = 1 ; i< values.size();i++){
//            if(values.get(i)!=null)
//                sql += ", '" + String.valueOf(values.get(i))+"'";
//            else
//                sql += ", " + String.valueOf(values.get(i));
//        }
//        sql+=")";
        statement.execute(sql);
        return true;
    }

    /**
     * 更新指令
     * @return
     */
    public boolean update(Connection connection,String tableName,Map<String,String> map,Map<String,String> where,List<Boolean> andOr) throws SQLException {
        Statement statement = connection.createStatement();
//        AtomicReference<String> sql = new AtomicReference<>("UPDATE " + tableName + " SET");
        //提取Sql指令
//        map.keySet().stream()
//                .map(
//                        e->{
//                            return " "+e.toString()+" = '"+map.get(e.toString()) + "' and";
//                        }
//                )
//                .forEach(
//                        e->{
//                            String temp = sql.get();
//                            temp += e;
//                            sql.set(temp);
//                        }
//                );
//        String strSql = sql.get();
        String strSql = "UPDATE " + tableName + " SET"+SqlStringUtil.toUpdate(map) +" WHERE";
//        sql.set(strSql);
        //提取Sql指令
//        where.keySet().stream()
//                .map(e->{
//                    return " "+e.toString()+" = "+where.get(e.toString());
//                })
//                .forEach(e->{
//                    String temp = sql.get();
//                    temp +=e.toString();
//                    sql.set(temp);
//                });
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
