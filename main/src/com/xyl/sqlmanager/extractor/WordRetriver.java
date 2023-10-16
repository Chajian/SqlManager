package com.xyl.sqlmanager.extractor;

import com.xyl.sqlmanager.util.WordUtils;

import java.util.*;

/**
 * 单词检索器
 * @author XieYanglin
 */
public class WordRetriver {
    /**
     * 关键字
     */
    static Set<String> keys = new HashSet<>();

    WhereHandler whereHandler;

    /**
     * 表名
     */
    String tableName;
    /**
     * 检索字段
     */
    List<String> columns;

    /**
     * 关键字缓存栈
     */
    Stack<String> stackCache = new Stack<>();

    String currentStatus = "";

/*静态初始化*/
    static {
        keys.add("SELECT");
        keys.add("FROM");
        keys.add("JOIN");
        keys.add("WHERE");
        keys.add("GROUP");
        keys.add("HAVING");
        keys.add("ORDER");
        keys.add("LIMIT");

    }
    /*非静态初始化*/
    {
    }

    public void Retriver(String sql){
        List<String> words = WordUtils.seperateWords(sql);
        //DML类型判断
        switch (words.get(0)){
            case "SELECT":
                selectRetriver(words);
                break;
        }


    }


    public void selectRetriver(List<String> words){
        whereHandler = new WhereHandler();
        whereHandler.setStatus(WhereHandler.WhereStatus.NEXT);
        for(int i = 0 ; i < words.size() ;i++){
            String word = words.get(i);
            //状态分析
            if(keys.contains(word.toUpperCase())) {
                stackCache.push(word);//关键词入栈
                currentStatus = word.toUpperCase();
            }
            else {
            //状态处理
                switch (currentStatus) {
                    case "WHERE"://处理where结构
                        handlerWhere(word);

                        break;
                }
            }
        }
    }

    public void handlerWhere(String word){
        whereHandler.handler(word);
    }

}
