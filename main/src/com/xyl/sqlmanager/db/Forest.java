package com.xyl.sqlmanager.db;

/**
 * 树 --- 单向树
 * 左-分支
 * 右-树
 */
public class Forest<T> {
    Node<T> root;


    public Forest(T value){
        root = new Node();
        root.setValue(value);
    }
    public Node<T> getRoot() {
        return root;
    }

    //插入左子树
    public void insertLeft(Node<T> node,T value){
        Node<T> next = node.getLeft();//左子树
        if(next==null) {
            Node<T> target = new Node();
            target.setValue(value);
            node.setLeft(target);
        }
        else
            insertLeft(next,value);
    }
    //插入右子树
    public void insertRight(Node<T> node,T value){
        Node<T> next = node.getRight();//右子树
        if(next==null) {
            Node<T> target = new Node();
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
    public Node<T> searchDLR(Node<T> node,String value){
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
    public Node<T> searchDRL(Node<T> node,String value){
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

    /**
     * 搜索森林
     * @param node
     * @param value 森林名
     * @return
     */
    public Node<T> searchForest(Node<T> node,String value){
        if(node.getValue().equals(value)){
            return node;
        }
        else if(node.getRight()!=null){
            return searchDRL(node.getRight(),value);
        }
        return null;
    }

    /**
     * 搜索树
     * @param node 森林
     * @param value 树名
     * @return
     */
    public Node searchTree(Node<T> node,String value){
        if(node.getValue().equals(value)){
            return node;
        }
        else if(node.getLeft()!=null){
            return searchDRL(node.getLeft(),value);
        }
        return null;
    }
}
