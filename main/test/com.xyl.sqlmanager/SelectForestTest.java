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
        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),"id");
        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),",card_id");
        selectTree.getRoot().insertRight(selectTree.getRoot().getRoot(),"from");
        Node node = selectTree.getRoot().searchDRL(selectTree.getRoot().getRoot(),"from");
        selectTree.getRoot().insertLeft(node,"user_base_info");
    }

    @Test
    public void generateSql(){

        System.out.println(selectTree.generationSql(selectTree.getRoot().getRoot()));
    }


}
