package com.xyl.sqlmanager.db;

/**
 * 查询树
 */
public class SelectForest implements SqlGenerator {

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
            return node.getValue()+" "+generationSql(node.getLeft())+" "+generationSql(node.getRight());
        }
        else{
            return node.getValue();
        }
    }


    @Override
    public String generate() {
        return generationSql(getRoot().getRoot());
    }

    @Override
    public void insertNode(String value) {
        root.insertRight(root.getRoot().getRight(),value);
    }

    @Override
    public void insertContent(String nodeName,String value) {
        Node node = root.searchForest(root.getRoot(),nodeName);
        root.insertLeft(node,value);
    }

    @Override
    public Node searchNode(String nodeName) {
        return root.searchForest(root.getRoot(),nodeName);
    }
}
