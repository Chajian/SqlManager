package com.xyl.sqlmanager.db;

/**
 * sql 语句生成器
 */
public interface SqlGenerator {
    String generate();

    Node insertNode(String value);

    Node insertLeaf(String nodeName,String value);



    /**
     * 生成forest的root根
     * @param node
     * @param sql
     * @return
     */
    Node generate(Node node,String sql);
}
