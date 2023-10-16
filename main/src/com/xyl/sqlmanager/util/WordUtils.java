package com.xyl.sqlmanager.util;

import java.util.ArrayList;
import java.util.List;

public class WordUtils {

    /**
     * 单词拆分
     * @return
     */
    public static List<String> seperateWords(String sql){
        List<String> words = new ArrayList<>();
        char[] chars = sql.toCharArray();
        String word = "";
        for(int i = 0 ; i < chars.length ;i++){
            if(chars[i]==' '||i==chars.length-1){//分割单词
                words.add(word);
                word = "";
                continue;
            }
            word+=chars[i];
        }
        return words;
    }
}
