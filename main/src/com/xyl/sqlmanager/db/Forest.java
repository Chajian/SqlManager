package com.xyl.sqlmanager.db;

/**
 * 树 --- 单向树
 * 左-分支
 * 右-树
 */
public class Forest {
    Node root;


    public Forest(String value){
        root = new Node();
        root.setValue(value);
    }
    public Node getRoot() {
        return root;
    }

    //插入左子树
    public void insertLeft(Node node,String value){
        Node next = left(node);//左子树
        if(next==null) {
            Node target = new Node();
            target.setValue(value);
            node.setLeft(target);
        }
        else
            insertLeft(next,value);
    }
    //插入右子树
    public void insertRight(Node node,String value){
        Node next = right(node);//右子树
        if(next==null) {
            Node target = new Node();
            target.setValue(value);
            node.setRight(target);
        }
        else
            insertRight(next,value);
    }

    /**
     * 根左右遍历
     * @param node
     * @return
     */
    public Node searchDLR(Node node,String value){
        if(node.getValue().equals(value)){
            return node;
        }
        else if(node.getLeft()!=null){
            return searchDLR(node.getLeft(),value);
        }
        else if(node.getLeft()!=null){
            return searchDLR(node.getRight(),value);
        }
        return null;
    }

    /**
     * 根右左遍历
     * @param node
     * @return
     */
    public Node searchDRL(Node node,String value){
        if(node.getValue().equals(value)){
            return node;
        }
        else if(node.getRight()!=null){
            return searchDRL(node.getRight(),value);
        }
        else if(node.getLeft()!=null){
            return searchDRL(node.getLeft(),value);
        }
        return null;
    }

    public Node left(Node node){
        return node.getLeft();
    }
    public Node right(Node node){
        return node.getRight();
    }
}
