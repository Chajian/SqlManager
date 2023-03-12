package com.xyl.sqlmanager;

import com.xyl.sqlmanager.db.Node;
import com.xyl.sqlmanager.db.SelectForest;
import org.junit.Before;
import org.junit.Test;


/**
 * select查询森林测试用例
 */
public class SelectForestTest {
    SelectForest selectTree;

    @Before
    public void init(){
        selectTree = new SelectForest();
//        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),"id");
//        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),",card_id");
//        selectTree.getRoot().insertRight(selectTree.getRoot().getRoot(),"from");
//        Node node = selectTree.getRoot().searchDRL(selectTree.getRoot().getRoot(),"from");
//        selectTree.getRoot().insertLeft(node,"user_base_info");
    }

    /**
     * 通过Forest构建sql
     */
    @Test
    public void generateSql(){
        selectTree.insertLeaf("select","id").insertLeft(",name").insertLeft(",card_id");
        selectTree.insertNode("from").insertLeft("user_base_info");
        selectTree.insertNode("where").insertLeft("id").insertLeft("<").insertLeft("100");
        System.out.println(selectTree.generate());
    }

    /**
     * 通过sql语句构建Forest
     */
    @Test
    public void generateForest(){
        String sql = "select id , name , card_id from user_base_info where id < 100";
        Node root = selectTree.generate(selectTree.getRoot(),sql);
        System.out.println(selectTree.generationSql(root));

    }



}
