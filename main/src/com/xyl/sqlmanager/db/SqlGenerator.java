package com.xyl.sqlmanager.db;

/**
 * sql 语句生成器
 */
public interface SqlGenerator {
    String generate();

    void insertNode(String value);

    void insertContent(String nodeName,String value);

    Node searchNode(String nodeName);

    Forest generate(String sql);
}
