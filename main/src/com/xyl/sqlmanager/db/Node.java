package com.xyl.sqlmanager.db;

/**
 * 节点
 *
 * feature
 * 1. add
 *
 *
 */
public class Node<T> {
    private T value;
    private Node<T> left;
    private Node<T> right;


    public Node(){

    }

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node insertRight(Node node,Node<T> right){
        if(node.getRight()!=null){
            insertRight(node.getRight(),right);
        }
        else {
            node.setRight(right);
            return right;
        }
        return null;
    }

    public Node insertRight(String right){
        if(this.getRight()!=null){
            return insertRight(this.getRight(),new Node(right));
        }
        else {
            Node temp = new Node(right);
            this.setRight(temp);
            return temp;
        }
    }


    public Node insertLeft(Node node,Node<T> left){
        if(node.getLeft()!=null){
            insertLeft(node.getLeft(),left);
        }
        else {
            node.setLeft(left);
            return left;
        }
        return null;
    }

    public Node insertLeft(Node node,String left){
        if(node.getLeft()!=null){
            return insertLeft(node.getLeft(),new Node(left));
        }
        else {
            Node temp = new Node(left);
            node.setLeft(temp);
            return temp;
        }
    }

    public Node insertLeft(String left){
        if(this.getLeft()!=null){
            return insertLeft(this.getLeft(),new Node(left));
        }
        else {
            Node temp = new Node(left);
            this.setLeft(temp);
            return temp;
        }
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


}
