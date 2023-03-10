package com.xyl.sqlmanager.db;

/**
 * 查询树
 */
public class SelectForest {

    Forest root;

    public SelectForest() {
        root = new Forest("select");
    }

    public Forest getRoot() {
        return root;
    }

    public String generationSql(Node node){
        if(node==null)
            return "";
        else if(node.getLeft()!=null||node.getRight()!=null){
            return node.getValue()+generationSql(node.getLeft())+generationSql(node.getRight());
        }
        else{
            return node.getValue()+" ";
        }
    }


}
