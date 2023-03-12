package com.xyl.sqlmanager.db;

import java.util.Arrays;

/**
 * 查询树
 */
public class SelectForest implements SqlGenerator {

    Node root;
    private String[] keys = {"select","from","where","order","group","LIMIT"};


    public SelectForest() {
        root = new Node("select");
    }

    public Node getRoot() {
        return root;
    }

    public String generationSql(Node node){
        if(node==null)
            return "";
        else if(node.getLeft()!=null||node.getRight()!=null){
            return node.getValue()+" "+generationSql(node.getLeft())+" "+generationSql(node.getRight());
        }
        else{
            return node.getValue().toString();
        }
    }

    public boolean isKey(String s){
        for(int i = 0 ; i < keys.length ; i++)
            if(keys[i].equalsIgnoreCase(s))
                return true;
        return false;
    }


    @Override
    public String generate() {
        return generationSql(getRoot());
    }

    @Override
    public Node insertNode(String value) {
        return root.insertRight(value);
    }

    @Override
    public Node insertLeaf(String nodeName,String value) {
        Node node = root.searchDRL(root,nodeName);
        return root.insertLeft(node,value);
    }





    @Override
    public Node generate(Node node,String sql) {
        String[] s = sql.split(" ");
        Node last = null;
        for(int i = 1 ; i < s.length ; i++){
            if(isKey(s[i])){
                Node next = new Node();
                next.setValue(s[i]);
                node.insertRight(node,next);
                last = next;
            }
            else{
                Node leaf = new Node();
                leaf.setValue(s[i]);
                if(last!=null)
                    last.insertLeft(last,leaf);
                else
                    node.insertLeft(node,leaf);
            }
        }

        return node;
    }
}
