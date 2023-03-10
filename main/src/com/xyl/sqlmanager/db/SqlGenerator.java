package com.xyl.sqlmanager.db;

/**
 *
 */
public interface SqlGenerator {
    String generate();

    void insertNode(String value);

    void insertContent(String nodeName,String value);

    Node searchNode(String nodeName);
}
