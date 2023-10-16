package com.xyl.sqlmanager.extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * where状态处理器
 */
public class WhereHandler {
    static Set<String> calculator = new HashSet<>();
    static Set<String> nextColumn = new HashSet<>();
    /*
    0 读入字段
    1 操作符
    2 入参
     */
    WhereStatus status = WhereStatus.KEY;
    /**
     * 入参字段
     */
    List<String> inputColumns = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    List<String> oprator = new ArrayList<>();

    /*静态初始化*/
    static {
        calculator.add(">");
        calculator.add("<");
        calculator.add("=");
        calculator.add(">=");
        calculator.add("<=");
        calculator.add("LIKE");
        calculator.add("BETWEEN");
        calculator.add("IS");
        calculator.add("IN");

        nextColumn.add("AND");
        nextColumn.add("OR");
    }


    /**
     * where状态处理器
     * @param word 单词
     */
    public void handler(String word){
        //状态处理
        if(calculator.contains(word.toUpperCase())){//如果当前work是操作符的话，表示入参
            status = WhereStatus.OPERATOR;
        }
        else if(nextColumn.contains(word.toUpperCase())){
            if(oprator.get(oprator.size()-1).equals("BETWEEN")){
                status = WhereStatus.OPERATOR;
            }
            else {
                status = WhereStatus.NEXT;
            }
        }
        else if(status == WhereStatus.NEXT){
            status = WhereStatus.KEY;
        }
        else if(status == WhereStatus.OPERATOR){
            status = WhereStatus.VALUE;
        }

        //数据处理
        switch (status){
            case KEY:
                keys.add(word);
                break;
            case VALUE:
                inputColumns.add(word);
                break;
            case OPERATOR:
                oprator.add(word);
                break;
        }
    }

    public WhereStatus getStatus() {
        return status;
    }

    public void setStatus(WhereStatus status) {
        this.status = status;
    }

    enum WhereStatus{
        OPERATOR,
        KEY,
        VALUE,
        NEXT


    }

}
