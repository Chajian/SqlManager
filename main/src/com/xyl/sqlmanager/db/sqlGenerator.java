package com.xyl.sqlmanager.db;

/**
 *
 */
public interface sqlGenerator {
    Forest generate();

    void insertNode(String value);

    void insertContent(String value);
}
