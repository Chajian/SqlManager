package com.xyl.sqlmanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SqlDriver {

    Connection getConnection() throws SQLException;

    List<String> showTables(Connection connection) throws SQLException;

    List<String> showDatabases(Connection connection) throws SQLException;

    boolean useDataBase(Connection connection,String db) throws SQLException;

    List<String> showTableColumnNames(Connection connection,String tableName) throws SQLException;

}
