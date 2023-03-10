package com.xyl.sqlmanager;

import com.xyl.sqlmanager.db.Node;
import com.xyl.sqlmanager.db.SelectForest;
import org.junit.Before;
import org.junit.Test;


public class SelectForestTest {
    SelectForest selectTree;

    @Before
    public void init(){
        selectTree = new SelectForest();
        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),"name");
        selectTree.getRoot().insertLeft(selectTree.getRoot().getRoot(),"age");
        selectTree.getRoot().insertRight(selectTree.getRoot().getRoot(),"from");
        Node node = selectTree.getRoot().searchDRL(selectTree.getRoot().getRoot(),"from");
        selectTree.getRoot().insertLeft(node,"tt");
        selectTree.getRoot().insertLeft(node,"bb");
    }

    @Test
    public void generateSql(){

        System.out.println(selectTree.generationSql(selectTree.getRoot().getRoot()));
    }


}
